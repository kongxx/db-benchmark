package db.benchmark.cli.bi;

import db.benchmark.cli.Command;

public class Main {
	public static void main(String[] args) throws Exception {
		CommandParser parser = new CommandParser();
		Command command = parser.parse(args);
		command.execute();
	}
}
