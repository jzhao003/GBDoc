package gbdoc.base;

import gbdoc.db.DBUtils;
import gubo.db.DaoManager;

import java.sql.SQLException;

import javax.sql.DataSource;

public class ApplicationContext {

	public DataSource getDataSource() throws SQLException {
		return DBUtils.getDataSource();
	}

	private final DaoManager daoManager = new DaoManager();

	public DaoManager getDaoManager() throws SQLException {
		return daoManager;
	}

}
