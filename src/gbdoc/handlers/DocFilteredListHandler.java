package gbdoc.handlers;

import java.sql.Connection;
import java.sql.SQLException;

import org.glassfish.grizzly.http.server.Request;

import gbdoc.db.DBUtils;
import gubo.db.ISimplePoJo;
import gubo.http.grizzly.handlers.FilteredListHandler;

public class DocFilteredListHandler extends FilteredListHandler {

	public DocFilteredListHandler(Class<? extends ISimplePoJo> clazz) {
		super(clazz);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		return DBUtils.getHikariConnection();
	}

	@Override
	public Long authCheck(Request request) throws Exception {
		return 0L;
	}
}
