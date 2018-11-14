package gbdoc.handlers;

import gbdoc.base.ApplicationContext;
import gbdoc.db.StandardSection;
import gubo.db.SimplePoJoDAO;
import gubo.http.grizzly.NannyHttpHandler;
import gubo.http.querystring.QueryStringField;

import java.sql.Connection;
import java.sql.SQLException;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

public class GetSectionTreeHandler extends NannyHttpHandler {
	ApplicationContext appCtx = null;

	@Override
	public Connection getConnection() throws SQLException {
		return appCtx.getDataSource().getConnection();
	}

	public GetSectionTreeHandler(ApplicationContext appCtx) {
		this.needLogin = false;
		this.appCtx = appCtx;
	}

	public class SectionTreeHandlerParameter {
		@QueryStringField(name = "standard_id")
		public long standardId;

	}

	@Override
	public Object doGet(Request req, Response res) throws Exception {
		SectionTreeHandlerParameter p = new SectionTreeHandlerParameter();
		this.bindParameter(req, p);
		SimplePoJoDAO dao = this.appCtx.getDaoManager().getDao(
				StandardSection.class);

		dao.loadPoJo(this.appCtx.getDataSource(),
				"select * from `standard` where standard_id=?", p.standardId);

		return null;
	}
}
