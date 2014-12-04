package my;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Formatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang.math.RandomUtils;

public class InsertTest {

	private static Logger logger = Logger.getLogger(InsertTest.class.getName());

	private static final String TABLE_NAME = "BENCHMARK_COL";

	private int batchSize;

	private int concurrent;

	private int sampling;

	public static void main(String[] args) throws Exception {
		printHeader();
		new InsertTest(1000, 1).run();
		new InsertTest(1000, 1).run();
		new InsertTest(2000, 1).run();
		new InsertTest(5000, 1).run();
		new InsertTest(10000, 1).run();
	}

	public InsertTest(final int batchSize, final int concurrent) throws Exception {
		this.batchSize = batchSize;
		this.concurrent = concurrent;
		this.sampling = 100;
	}

	public InsertTest(final int batchSize, final int concurrent, final int sampling) throws Exception {
		this.batchSize = batchSize;
		this.concurrent = concurrent;
		this.sampling = sampling;
	}

	public void run() throws Exception {
		final List<Long> results = Collections.synchronizedList(new ArrayList<Long>());
		final CountDownLatch startGate = new CountDownLatch(concurrent);
		final CountDownLatch endGate = new CountDownLatch(concurrent);

		for (int idxConcurrent = 0; idxConcurrent < concurrent; idxConcurrent++) {
			new Thread(new Runnable() {
				public void run() {
					startGate.countDown();
					try {
						long time = execute();
						long avg = batchSize * sampling * 1000 / time;;
						results.add(Long.valueOf(avg));
					} catch(Exception ex) {
						ex.printStackTrace();
					} finally {
						endGate.countDown();
					}
				}
			}).start();
		}
		endGate.await();

		Collections.sort(results);

		printResult(batchSize, concurrent, results);
	}

	public long execute() throws Exception {
		Connection conn = getConnection();
		Map<String, Integer> columns = queryTableColumns(conn);
		String insertSQL = generateInsertSQL(columns);
		PreparedStatement ps = conn.prepareStatement(insertSQL);
		try {
			long start = System.currentTimeMillis();
			for (int i = 0; i < sampling; i++) {
				execute(conn, ps, columns);
			}
			long stop = System.currentTimeMillis();
			return stop - start;
		} catch(Exception ex) {
			logger.log(Level.SEVERE, null, ex);
			DbUtils.rollbackAndCloseQuietly(conn);
			throw ex;
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

	public void execute(Connection conn, PreparedStatement ps, Map<String, Integer> columns) throws Exception {
		try {
			for (int idx = 0; idx < batchSize; idx++) {
				int idxColumn = 1;
				for (String column : columns.keySet()) {
					if (column.equalsIgnoreCase("ID")) {
						ps.setObject(idxColumn, UUID.randomUUID().toString());
					} else {
						ps.setObject(idxColumn, generateColumnValue(columns.get(column)));
					}
					idxColumn ++;
				}
				ps.addBatch();
			}
			ps.executeBatch();
			conn.commit();

			ps.clearBatch();
		} catch (SQLException ex) {
			logger.log(Level.SEVERE, null, ex);
			if (null != ex.getNextException()) {
				logger.log(Level.SEVERE, null, ex.getNextException());
			}
			DbUtils.rollback(conn);
		}
	}

	private String generateInsertSQL(Map<String, Integer> columns) throws SQLException {
		StringBuilder sb = new StringBuilder();
		StringBuffer sbColumns = new StringBuffer();
		StringBuffer sbValues = new StringBuffer();

		sb.append("INSERT INTO ").append(TABLE_NAME);

		for (String column : columns.keySet()) {
			if (sbColumns.length() > 0) {
				sbColumns.append(",");
				sbValues.append(",");
			}
			sbColumns.append(column);
			sbValues.append("?");
		}
		sb.append("(").append(sbColumns).append(")");
		sb.append("VALUES");
		sb.append("(").append(sbValues).append(")");
		return sb.toString();
	}

	private Map<String, Integer> queryTableColumns(Connection conn) throws Exception {
		Map<String, Integer> columns = null;
		try {
			QueryRunner queryRunner = new QueryRunner();
			String sql = "SELECT * FROM " + TABLE_NAME + " WHERE 1=0";
			columns = queryRunner.query(conn, sql, new ResultSetHandler<Map<String, Integer>>() {
				@Override
				public Map<String, Integer> handle(ResultSet rs) throws SQLException {
					Map<String, Integer> columns = new LinkedHashMap<String, Integer>();
					ResultSetMetaData rsmd = rs.getMetaData();
					for (int i = 1; i <= rsmd.getColumnCount(); i++) {
						columns.put(rsmd.getColumnName(i), rsmd.getColumnType(i));
					}
					return columns;
				}
			});
		} finally {
			DbUtils.closeQuietly(conn);
		}
		return columns;
	}

	private Object generateColumnValue(int type) {
		Object obj = null;
		switch (type) {
			case Types.DECIMAL:
			case Types.NUMERIC:
			case Types.DOUBLE:
			case Types.FLOAT:
			case Types.REAL:
			case Types.BIGINT:
			case Types.TINYINT:
			case Types.SMALLINT:
			case Types.INTEGER:
				obj = RandomUtils.nextInt(10000);
				break;
			case Types.DATE:
				obj = Calendar.getInstance().getTime();
				break;
			case Types.TIMESTAMP:
				obj = new Timestamp(System.currentTimeMillis());
				break;
			default:
				obj = String.valueOf(RandomUtils.nextInt(10000));
				break;
		}
		return obj;
	}

	private Connection getConnection() throws Exception {
		Class.forName("com.ibm.db2.jcc.DB2Driver");
		String url = "jdbc:db2://padev3:50000/padb";
		Connection conn = DriverManager.getConnection(url, "db2inst", "Letmein");

//		Class.forName("com.vertica.jdbc.Driver");
//		String url = "jdbc:vertica://9.111.251.152:5433/padb";
//		Connection conn = DriverManager.getConnection(url, "pauser", "papassword");

		conn.setAutoCommit(false);
		return conn;
	}

	private static void printHeader() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append(new Formatter().format("%12s|%12s|%10s|%10s|%10s", "BATCH_SIZE", "CONCURRENT", "AVG (r/s)", "MIN (r/s)", "MAX (r/s)"));
		System.out.println(sb.toString());
	}

	private static void printResult(int batch, int concurrent, List<Long> results) {
		Long total = Long.valueOf(0);
		for (Long result : results) {
			total += result;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(new Formatter().format("%12s|%12s|%10s|%10s|%10s", batch, concurrent, (total/results.size()), results.get(0), results.get(results.size() - 1)));
		System.out.println(sb.toString());
	}
}
