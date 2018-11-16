package gbdoc.handlers;

import java.sql.Connection;
import java.sql.SQLException;

import org.glassfish.grizzly.http.server.Request;

import gbdoc.db.DBUtils;
import gubo.db.ISimplePoJo;
import gubo.http.grizzly.handlers.CreateSimplePojoHandler;

public class CreateRecordsHandler extends CreateSimplePojoHandler {

	public CreateRecordsHandler(Class<? extends ISimplePoJo> clazz) {
		super(clazz);
		// TODO Auto-generated constructor stub
	}
	
	public Long authCheck(Request request) throws Exception {
		return null;
	}
	
	public Connection getConnection() throws SQLException {
		return DBUtils.getHikariConnection();
	}
	
}
