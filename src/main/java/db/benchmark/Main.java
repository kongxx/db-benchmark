package db.benchmark;

import db.benchmark.cli.Command;
import db.benchmark.cli.CommandParser;

public class Main {
	public static void main(String[] args) throws Exception {
		CommandParser parser = new CommandParser();
		Command command = parser.parse(args);
		command.execute();
	}
}
