package db.benchmark.cli.bi;

import db.benchmark.Action;
import db.benchmark.ExecuteResults;
import db.benchmark.ExecuteResultsFormatter;
import db.benchmark.PerformanceTest;
import db.benchmark.action.InsertAction;
import db.benchmark.cli.Command;

public class InsertCommand implements Command {
	private String table;
	private int batchSize;
	private int concurrentCount;
	private int sampleCount;

	public InsertCommand(String table, int batchSize, int concurrentCount, int sampleCount) {
		this.table = table;
		this.batchSize = batchSize;
		this.concurrentCount = concurrentCount;
		this.sampleCount = sampleCount;
	}

	public InsertCommand(int batchSize, int concurrentCount, int sampleCount) {
		this.batchSize = batchSize;
		this.concurrentCount = concurrentCount;
		this.sampleCount = sampleCount;
	}

	@Override
	public String execute() {
		try {
			ExecuteResults results = new ExecuteResults();
			PerformanceTest test = new PerformanceTest();
			Action action = null;
			int nBatchSize = 10;
			if (table != null) {
				action = new InsertAction(table, batchSize, nBatchSize);
			} else {
				action = new InsertAction(batchSize, nBatchSize);
			}
			String subject = "Insert test[batch: " + batchSize + ", total: "+ (batchSize * nBatchSize) +"]";
			results.addTestResult(test.execute(subject, action, concurrentCount, sampleCount));
			ExecuteResultsFormatter formatter = new ExecuteResultsFormatter(results);
			System.out.println(formatter.toString());
		} catch(Exception ex) {
			return CMD_FAILURE;
		}
		return CMD_SUCCESS;
	}
}
