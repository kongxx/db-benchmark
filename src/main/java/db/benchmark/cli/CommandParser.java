package db.benchmark.cli;

import db.benchmark.cli.bi.InsertCommand;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandParser {
	
	private final Options options;
	private final Option optHelp;
	private final Option optVersion;
	
	public CommandParser() {
		optHelp = new Option("h", false, "Help ");
		optVersion = new Option("v", false, "Print version information.");
		
		options = new Options();
		options.addOption(optHelp);
		options.addOption(optVersion);
	}
	
	public Command parse(String[] args) {
		CommandLineParser parser = new BasicParser();
		CommandLine cl;
		Command cmd = null;

//		try {
//			cl = parser.parse(options, args);
//		} catch (ParseException ex) {
//			return new HelpCommand(); 
//		}
//		
//		Option[] opts = cl.getOptions();
//		if (opts == null) {
//			cmd = new HelpCommand();
//		} else if (cl.hasOption(optHelp.getOpt())) {
//			cmd = new HelpCommand();
//		} else if (cl.hasOption(optVersion.getOpt())) {
//			cmd = new VersionCommand();
//		} else if (cl.hasOption(optInsert.getOpt())) {
//			String[] values = cl.getOptionValues(optInsert.getOpt());
//			int length = values.length;
//			if (length == 4) {
//				int totalSize = Integer.parseInt(values[0]);
//				int batchSize = Integer.parseInt(values[1]);
//				int concurrentCount = Integer.parseInt(values[2]);
//				int sampleCount = Integer.parseInt(values[3]);
//				cmd = new InsertCommand(totalSize, batchSize, concurrentCount, sampleCount);
//			} else {
//				cmd = new HelpCommand();
//			}
//		} else {
//			cmd = new HelpCommand();
//		}
		
		return cmd;
	}
	
	public Options getOptions() {
		return this.options;
	}
}
