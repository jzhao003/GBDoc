package gbdoc.handlers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import gbdoc.db.DBUtils;
import gbdoc.db.StandardSection;
import gubo.db.ISimplePoJo;
import gubo.http.grizzly.NannyHttpHandler;
import gubo.http.querystring.QueryStringBinder;
import gubo.jdbc.mapping.ResultSetMapper;

public class SectionTreeListHandler extends NannyHttpHandler {

	Class<? extends ISimplePoJo> clazz;

	public SectionTreeListHandler(Class<? extends ISimplePoJo> clazz) {
		this.clazz = clazz;
	}

	protected final List<Object> doFilter(Map<String, String> params) throws Exception {
		Connection dbconn = this.getConnection();
		try {
			dbconn.setAutoCommit(true);
			return ResultSetMapper.loadPoJoList(dbconn, clazz, params);
		} finally {
			dbconn.close();
		}
	}

	private JSONObject buildJsonTree(List<Object> list) {
		JSONObject json = new JSONObject();
		JSONArray members = new JSONArray();

		// convert to json object
		/*
		 * var myTree = [ { text: "Item 1", nodes: [ { text: "Item 1-1",
		 * 
		 * nodes: [ { text: "Item 1-1-1" }, { text: "Item 1-1-2" } ] }, { text:
		 * "Item 1-2" } ] }, { text: "Item 2" }, { text: "Item 3" } ];
		 */
		StandardSection standardSection = null;
		for (int i = 0; i < list.size(); i++) {
			JSONObject node = new JSONObject();
			standardSection = (StandardSection) list.get(i);
			node.put("text", standardSection.section_number+standardSection.section_content);
			node.put("id", standardSection.id);
			members.put(node);
		}
		json.put("members", members);
		return json;

	}

	/**
	 * Subclasses can override this method to do custom check, add extra conditions,
	 * then call super.doGet
	 * 
	 **/
	@Override
	public Object doGet(Request request, Response response) throws Exception {
		this.authCheck(request);
		Map<String, String> conditions = QueryStringBinder.extractParameters(request);
		List<Object> list = this.doFilter(conditions);
		return buildJsonTree(list);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return DBUtils.getHikariConnection();
	}

	@Override
	public Long authCheck(Request request) throws Exception {
		return 0L;
	}

	public void sendJson(Object o, Request req, Response response) throws IOException {
		if (o == null) {
			response.setStatus(HttpStatus.METHOD_NOT_ALLOWED_405);
		}
		this.setCrossDomain(response);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		response.getWriter().write(o.toString());
	}

}
