package db.benchmark;

import java.io.Serializable;

public class ExecuteResult implements Serializable {
	private String subject;
	private int concurrent;
	private int samples;
	private long avg;
	private long min;
	private long max;

	public ExecuteResult() {

	}

	public ExecuteResult(String subject, int concurrent, int samples, long avg, long min, long max) {
		this.subject = subject;
		this.concurrent = concurrent;
		this.samples = samples;
		this.avg = avg;
		this.min = min;
		this.max = max;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getConcurrent() {
		return concurrent;
	}

	public void setConcurrent(int concurrent) {
		this.concurrent = concurrent;
	}

	public int getSamples() {
		return samples;
	}

	public void setSamples(int samples) {
		this.samples = samples;
	}

	public long getAvg() {
		return avg;
	}

	public void setAvg(long avg) {
		this.avg = avg;
	}

	public long getMin() {
		return min;
	}

	public void setMin(long min) {
		this.min = min;
	}

	public long getMax() {
		return max;
	}

	public void setMax(long max) {
		this.max = max;
	}
}
