package db.benchmark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;

public class ExecuteResultsFormatter {

	private List<ExecuteResult> results = new ArrayList<ExecuteResult>();

	public ExecuteResultsFormatter(ExecuteResults results) {
		this.results = results.getResults();
	}

	public ExecuteResultsFormatter(ExecuteResult... results) {
		this.results = Arrays.asList(results);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\n\n");
		sb.append("################################################################################").append("\n");
		sb.append(" T E S T   R E S U L T").append("\n");
		sb.append("################################################################################").append("\n\n");
//		for (Entry<String, String> result : results.entrySet()) {
//			sb.append("==============================").append("\n");
//			sb.append(result.getKey()).append("\n");
//			sb.append("------------------------------").append("\n");
//			sb.append(result.getValue()).append("\n");
//			sb.append("==============================").append("\n");
//			sb.append("\n");
//		}

		int maxSubjectLength = 40;
		for (ExecuteResult result : results) {
			if (maxSubjectLength < result.getSubject().length()) {
				maxSubjectLength = result.getSubject().length();
			}
		}

		sb.append(new Formatter().format("%-"+maxSubjectLength+"s|%10s|%10s|%10s|%10s|%10s\n", "SUBJECT", "CONCURRENT", "SAMPLES", "AVG (ms)", "MIN (ms)", "MAX (ms)"));
		sb.append(new Formatter().format("%-"+maxSubjectLength+"s+%10s+%10s+%10s+%10s+%10s\n", "", "", "", "", "", "").toString().replaceAll(" ", "-"));
		for (ExecuteResult result : results) {
			sb.append(new Formatter().format("%-"+maxSubjectLength+"s|%10s|%10s|%10s|%10s|%10s\n",
					result.getSubject(),
					result.getConcurrent(),
					result.getSamples(),
					result.getAvg(),
					result.getMin(),
					result.getMax()));
			//sb.append(new Formatter().format("%-"+maxSubjectLength+"s+%10s+%10s+%10s+%10s+%10s\n", "", "", "", "", "", "").toString().replaceAll(" ", "-"));
		}

		return sb.toString();
	}
}
