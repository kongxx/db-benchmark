package db.benchmark;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class PerformanceTest {

	public ExecuteResult execute(final String subject, final Action action, final int concurrentCount, final int sampleCount) throws Exception {
		return this.execute(subject, action, concurrentCount, sampleCount, null, null);
	}

	public ExecuteResult execute(final String subject, final Action action, final int concurrentCount, final int sampleCount,
							  final Action preAction, final Action postAction) throws Exception {
		System.out.println(subject);

		final List<Long> times = Collections.synchronizedList(new ArrayList<Long>());

		for (int idxSample = 0; idxSample <= sampleCount; idxSample++) {
			if (preAction != null) {
				preAction.execute();
			}

			final CountDownLatch startGate = new CountDownLatch(concurrentCount);
			final CountDownLatch endGate = new CountDownLatch(concurrentCount);

			final boolean isFirstTime = (idxSample == 0)? true: false;
			if (isFirstTime) {
				System.out.println("Starting the " + idxSample + " time(s) sampling, this sampling will be ignored.");
			} else {
				System.out.println("Starting the " + idxSample + " time(s) sampling ...");
			}
			Thread.sleep(2000);

			for (int idxConcurrent = 0; idxConcurrent < concurrentCount; idxConcurrent++) {
				new Thread(new Runnable() {
					public void run() {
						startGate.countDown();

						try {
							long time = runIt(action);
							System.out.println("Time used: " + time + " ms");
							if (!isFirstTime) {
								times.add(Long.valueOf(time));
							}
						} catch(Exception ex) {
							ex.printStackTrace();
						} finally {
							endGate.countDown();
						}
					}
				}).start();
			}

			endGate.await();

			if (postAction != null) {
				postAction.execute();
			}

			Thread.sleep(5000);
		}

		Collections.sort(times);
		Long totalTime = Long.valueOf(0);
		for (Long time : times) {
			totalTime += time;
		}

		StringBuffer sb = new StringBuffer();
		sb.append("Sample: " + sampleCount).append("\n");
		sb.append("Concurrent: " + concurrentCount).append("\n");
		sb.append("Avg: " + (totalTime/times.size()) + " ms").append("\n");
		sb.append("Min: " + times.get(0) + " ms").append("\n");
		sb.append("Max: " + times.get(times.size() - 1) + " ms");

		System.out.println("====================");
		System.out.println(sb);
		System.out.println("====================");

		return new ExecuteResult(subject, concurrentCount, sampleCount, (totalTime/times.size()), times.get(0), times.get(times.size() - 1));
	}

	private long runIt(Action action) throws Exception {
		long start = System.currentTimeMillis();
		action.execute();
		long stop = System.currentTimeMillis();
		//System.out.println("Time used: " + (stop - start) + " ms");
		return stop - start;
	}
}
