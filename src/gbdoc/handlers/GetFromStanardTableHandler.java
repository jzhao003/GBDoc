package gbdoc.handlers;

import java.sql.Connection;
import java.sql.SQLException;

import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import gbdoc.base.ApplicationContext;
import gbdoc.db.StandardSection;
import gubo.db.SimplePoJoDAO;
import gubo.http.grizzly.NannyHttpHandler;
import gubo.http.querystring.QueryStringField;

public class GetFromStanardTableHandler  extends NannyHttpHandler {
	ApplicationContext appCtx = null;

	@Override
	public Connection getConnection() throws SQLException {
		return appCtx.getDataSource().getConnection();
	}

	public GetFromStanardTableHandler(ApplicationContext appCtx) {
		this.needLogin = false;
		this.appCtx = appCtx;
	}

	public class GetFromStanardParameter {
		@QueryStringField(name = "id")
		public long standardId;

	}

	@Override
	public Object doGet(Request req, Response res) throws Exception {
		GetFromStanardParameter p = new GetFromStanardParameter();
		this.bindParameter(req, p);
		SimplePoJoDAO dao = this.appCtx.getDaoManager().getDao(
				StandardSection.class);

		return dao.loadPoJo(this.appCtx.getDataSource(),
				"select * from `standard` where id=?", p.standardId);
	}
}
