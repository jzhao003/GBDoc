package gbdoc.base;

import gbdoc.db.DBUtils;

import java.sql.SQLException;

import javax.sql.DataSource;

public class ApplicationContext {

	public DataSource getDataSource() throws SQLException {
		return DBUtils.getDataSource();
	}
}
