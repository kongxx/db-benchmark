package db.benchmark;

import db.benchmark.action.InsertAction;
import java.io.File;

public class Test {
	public static void main(String[] args) throws Exception {
		System.out.println(DBUtils.getInstance().getConnection());
		File file = new File(Test.class.getResource("/conf/db.properties").getFile());
		
//		long start = System.currentTimeMillis();
//		List<String> ids = new ArrayList<String>(10000000);
//		for (int i = 0; i < 10000000; i++) {
//			String id = UUID.randomUUID().toString();
//			if (ids.contains(id)) {
//				System.out.println(id);
//				System.exit(-1);
//			}
//			ids.add(id);
//		}
//		long end = System.currentTimeMillis();
//		System.out.println("Time: " + (end - start));
//		System.out.println("Size: " + ids.size());
	}
	
	public static void main2(String[] args) throws Exception {
		ExecuteResults results = new ExecuteResults();
		PerformanceTest test = new PerformanceTest();

		int[] t = new int[]{1};
		Action action;

		for (int i = 0; i < t.length; i++) {
			action = new InsertAction(1, 10);
			results.addTestResult(test.execute("list session by application", action, t[i], 1));

//			action = new Action() {
//				@Override
//				public void execute() throws ExecuteException {
//					int idx = 0;
//					while(idx == 0) {
//						idx = RandomUtils.nextInt(10);
//					}
//					try {
//						Thread.sleep(idx * 1000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			};
//			results.addTestResult(test.execute("list task by session", action, t[i], 10));
		}

		ExecuteResultsFormatter formatter = new ExecuteResultsFormatter(results);
		System.out.println(formatter.toString());
	}
}
