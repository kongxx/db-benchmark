package db.benchmark.cli.bi;

import db.benchmark.Action;
import db.benchmark.ExecuteResults;
import db.benchmark.ExecuteResultsFormatter;
import db.benchmark.PerformanceTest;
import db.benchmark.action.InsertAction;
import db.benchmark.cli.Command;

public class InsertCommand implements Command {
	private final int batchSize;
	private final int concurrentCount;
	private final int sampleCount;
	
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
			Action action = new InsertAction(batchSize);
			String subject = "Insert test[batch size: " + batchSize + "]";
			results.addTestResult(test.execute(subject, action, concurrentCount, sampleCount));
			ExecuteResultsFormatter formatter = new ExecuteResultsFormatter(results);
			System.out.println(formatter.toString());
		} catch(Exception ex) {
			return CMD_FAILURE;
		}
		return CMD_SUCCESS;
	}
}
