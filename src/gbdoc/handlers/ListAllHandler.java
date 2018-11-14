package gbdoc.handlers;

import gbdoc.base.ApplicationContext;
import gubo.db.ISimplePoJo;

import java.sql.Connection;
import java.sql.SQLException;

public class ListAllHandler extends gubo.http.grizzly.handlers.ListAllHandler {

	ApplicationContext appCtx = null;

	@Override
	public Connection getConnection() throws SQLException {
		return appCtx.getDataSource().getConnection();
	}

	public ListAllHandler(Class<? extends ISimplePoJo> clazz,
			ApplicationContext appCtx) {
		super(clazz);
		this.needLogin = false;
		this.appCtx = appCtx;
	}

}
