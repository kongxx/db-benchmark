package db.benchmark;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExecuteResults implements Serializable {

	public List<ExecuteResult> results = Collections.synchronizedList(new ArrayList<ExecuteResult>());

	public List<ExecuteResult> getResults() {
		return results;
	}

	public void setResults(List<ExecuteResult> results) {
		this.results = results;
	}

	public void addTestResult(ExecuteResult result) {
		this.results.add(result);
	}

	public void addTestResults(ExecuteResults results) {
		for (ExecuteResult result : results.getResults()) {
			this.results.add(result);
		}
	}
}
