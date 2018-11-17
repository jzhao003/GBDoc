package gbdoc.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBUtils {

//	public static String dbUrl = "jdbc:mysql://localhost:3306/gb-doc?useUnicode=true&characterEncoding=UTF-8";
//	public static String dbUser = "root";
//	public static String dbPassword = "";

	public static HikariDataSource makeNewHikariDataSource() {
		HikariConfig config = new HikariConfig();
		
		Properties prop = new Properties();
		InputStream input = null;
		String filename = "conf/application.properties";
		try {
			input = new FileInputStream(filename);
			prop.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		config.setJdbcUrl(prop.getProperty("dbUrl"));
		config.setUsername(prop.getProperty("dbUser"));
		config.setPassword(prop.getProperty("dbPassword"));

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
