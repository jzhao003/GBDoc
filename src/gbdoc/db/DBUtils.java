package gbdoc.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBUtils {

	// public static String dbUrl = "";
	// public static String dbUser = "";
	// public static String dbPassword = "";

	public static String dbUrl = "jdbc:mysql://localhost:3306/gb-doc?useUnicode=true&characterEncoding=UTF-8";
	public static String dbUser = "root";
	public static String dbPassword = "";

	public static HikariDataSource makeNewHikariDataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(dbUrl);
		config.setUsername(dbUser);
		config.setPassword(dbPassword);
		// config.addDataSourceProperty("cachePrepStmts", "true");
		// config.addDataSourceProperty("prepStmtCacheSize", "250");
		// config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

		HikariDataSource ds = new HikariDataSource(config);
		return ds;
	}

	static HikariDataSource ds = null;

	public static Connection getHikariConnection() throws SQLException {
		if (ds == null) {
			ds = makeNewHikariDataSource();
		}
		return ds.getConnection();
	}

	public static DataSource getDataSource() throws SQLException {
		if (ds == null) {
			ds = makeNewHikariDataSource();
		}
		return ds;
	}
}
