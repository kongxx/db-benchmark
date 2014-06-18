package db.benchmark;

public class Executor {

	public static void execute(Action action) throws ExecuteException {
		long start = System.currentTimeMillis();
		action.execute();
		long stop = System.currentTimeMillis();
		System.out.println("Time used: " + (stop - start) + " ms");
	}

	public static void execute(String msg, Action action) throws ExecuteException {
		long start = System.currentTimeMillis();
		action.execute();
		long stop = System.currentTimeMillis();
		System.out.println(msg + ": " + (stop - start) + " ms");
	}
}
