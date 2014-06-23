package db.benchmark.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang.math.RandomUtils;

import db.benchmark.Action;
import db.benchmark.DBUtils;
import db.benchmark.ExecuteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InsertAction implements Action {

	private static Logger logger = Logger.getLogger(InsertAction.class.getName());
			
	private static final String TABLE_NAME = "BENCHMARK";
	
	private final int batchSize;
	
	public InsertAction(int batchSize) {
		this.batchSize = batchSize;
	}
	
	@Override
	public void execute() throws ExecuteException {
		Connection conn = null;
		try {
			conn = DBUtils.getInstance().getConnection(false);
		} catch(SQLException ex) {
			logger.log(Level.SEVERE, null, ex);
			throw new ExecuteException(ex.getMessage(), ex);
		}
		
		try {
			Map<String, Integer> columns = queryTableColumns();
			String sql = generateInsertSQL(columns);
			PreparedStatement ps = conn.prepareStatement(sql);
			
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
			DbUtils.rollbackAndCloseQuietly(conn);
			throw new ExecuteException(ex.getMessage(), ex);
		} finally {
			DbUtils.closeQuietly(conn);
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
	
	private Map<String, Integer> queryTableColumns() throws SQLException {
		Map<String, Integer> columns = null;
		Connection conn = null;
		try {
			conn = DBUtils.getInstance().getConnection();
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
			case Types.TIMESTAMP:
				obj = Calendar.getInstance().getTime();
				break;
			default:
				obj = "zzzzzzzzzzZZZZZZZZZZ_" + RandomUtils.nextInt(100);
				break;
		}
		return obj;
	}
}
