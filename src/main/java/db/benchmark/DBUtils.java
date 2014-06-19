package db.benchmark;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.io.IOUtils;

public class DBUtils {
	
	private static final DBUtils dbUtils = new DBUtils();
	
	private DataSource datasource = null;
	
	private DBUtils() {
		String config = System.getProperty("DB_BENCHMARK_HOME");
		if (config == null) {
			System.err.println("Cannot find environment variable: DB_BENCHMARK_HOME.");
			System.exit(-1);
		}
		
		InputStream is = null;
		try {
			Properties props = new Properties();
			File file = new File(config + "/conf/db.properties");
			is = new FileInputStream(file);
			props.load(is);
			datasource = BasicDataSourceFactory.createDataSource(props);
		} catch(Exception ex) {
			System.err.println("Failed to initialize database connection.");
			System.exit(-1);
		} finally {
			IOUtils.closeQuietly(is);
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
