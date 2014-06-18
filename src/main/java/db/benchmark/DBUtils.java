package db.benchmark;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

public class DBUtils {
	
	private static DBUtils dbUtils = new DBUtils();
	
	private DataSource datasource = null;
	
	private DBUtils() {
		try {
//			String driver = "org.gjt.mm.mysql.Driver";
//			String url = "jdbc:mysql://pac-2:3306/pac";
//			String username = "pacuser";
//			String password = "pacuser";
			
			String driver = "com.ibm.db2.jcc.DB2Driver";
			String url = "jdbc:db2://padev3:50000/pa913";
			String username = "db2inst";
			String password = "Letmein";
			
			Properties props = new Properties();
			props.setProperty("username", username);
			props.setProperty("password", password);
			props.setProperty("driverClassName", driver);
			props.setProperty("url", url);
			datasource = BasicDataSourceFactory.createDataSource(props);
			System.out.println(datasource.getConnection());
		} catch(Exception ex) {
			System.out.println("Failed to initialize database connection.");
			ex.printStackTrace();
			System.exit(-1);
		}
	}
	
	public static DBUtils getInstance() {
		return dbUtils;
	}
	
	public Connection getConnection() throws SQLException {
		Connection conn = datasource.getConnection();
		return conn;
	}
	
	public Connection getConnection(boolean autoCommit) throws SQLException {
		Connection conn = datasource.getConnection();
		conn.setAutoCommit(autoCommit);
		return conn;
	}
}
