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

public class InsertAction implements Action {

	private static final String TABLE_NAME = "BENCHMARK2";
	
	private final int totalSize;
	private final int batchSize;
	
	public InsertAction(int totalSize, int batchSize) {
		this.totalSize = totalSize;
		this.batchSize = batchSize;
	}
	
	@Override
	public void execute() throws ExecuteException {
		Map<String, Integer> columns = queryTableColumns();

		Connection conn = null;
		try {
			conn = DBUtils.getInstance().getConnection(false);
			String sql = generateInsertSQL(columns);
			PreparedStatement ps = conn.prepareStatement(sql);
			
			int loop = (totalSize / batchSize) + ((totalSize % batchSize == 0) ? 0 : 1);
			int insertedCount = 0;
			for (int i = 0; i < loop; i++) {
				for (int j = 0; j < batchSize; j++) {
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
					insertedCount ++;
					if (insertedCount >= totalSize) {
						break;
					}
				}
				ps.executeBatch();
				conn.commit();
				
				ps.clearBatch();
			}
		} catch (SQLException e) {
			throw new ExecuteException(e.getMessage(), e);
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
	
	private Map<String, Integer> queryTableColumns(){
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
		} catch (SQLException e) {
			e.printStackTrace();
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
