package db.benchmark.cli.bi;

import db.benchmark.cli.*;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandParser {
	
	private final Options options;
	private final Option optHelp;
	private final Option optTable;
	private final Option optBatch;
	private final Option optConcurrent;
	private final Option optSample;

	public CommandParser() {
		optHelp = new Option("h", false, "Help ");

		optTable = new Option("t", true, "The table name.");
		optTable.setArgs(1);
		optTable.setArgName("table name");

		optBatch = new Option("b", true, "The batch size.");
		optBatch.setArgs(1);
		optBatch.setArgName("batch size");
		
		optConcurrent = new Option("c", true, "The concurrent number.");
		optConcurrent.setArgs(1);
		optConcurrent.setArgName("concurrent number");
		
		optSample = new Option("s", true, "The sampling count.");
		optSample.setArgs(1);
		optSample.setArgName("sampling count");

		options = new Options();
		options.addOption(optHelp);
		options.addOption(optTable);
		options.addOption(optBatch);
		options.addOption(optConcurrent);
		options.addOption(optSample);
	}
	
	public Command parse(String[] args) {
		CommandLineParser parser = new BasicParser();
		CommandLine cl;
		Command cmd;

		try {
			cl = parser.parse(options, args);
		} catch (ParseException ex) {
			return new HelpCommand("bi", this.options); 
		}
		
		Option[] opts = cl.getOptions();
		if (opts != null) {
			if (cl.hasOption(optTable.getOpt())
				&& cl.hasOption(optBatch.getOpt())
				&& cl.hasOption(optConcurrent.getOpt())
				&& cl.hasOption(optSample.getOpt())
				) {
				String table = cl.getOptionValue(optTable.getOpt());
				int batchSize = Integer.parseInt(cl.getOptionValue(optBatch.getOpt()));
				int concurrentCount = Integer.parseInt(cl.getOptionValue(optConcurrent.getOpt()));
				int sampleCount = Integer.parseInt(cl.getOptionValue(optSample.getOpt()));
				cmd = new InsertCommand(table, batchSize, concurrentCount, sampleCount);
			} else if (cl.hasOption(optBatch.getOpt())
					&& cl.hasOption(optConcurrent.getOpt())
					&& cl.hasOption(optSample.getOpt())
					) {
				int batchSize = Integer.parseInt(cl.getOptionValue(optBatch.getOpt()));
				int concurrentCount = Integer.parseInt(cl.getOptionValue(optConcurrent.getOpt()));
				int sampleCount = Integer.parseInt(cl.getOptionValue(optSample.getOpt()));
				cmd = new InsertCommand(batchSize, concurrentCount, sampleCount);
			} else {
				cmd = new HelpCommand("bi", this.options);
			}
		} else {
			cmd = new HelpCommand("bi", this.options); 
		}
		
		return cmd;
	}
	
	public Options getOptions() {
		return this.options;
	}
}
