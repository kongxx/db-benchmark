package db.benchmark.cli;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

public class HelpCommand implements Command {

	private final String cmd;
	private final Options options;
	
	public HelpCommand(String cmd, Options options) {
		this.cmd = cmd;
		this.options = options;
	}
	
	@Override
	public String execute() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		CommandParser parser = new CommandParser();
		HelpFormatter formatter = new HelpFormatter();
		formatter.setSyntaxPrefix("Usage: ");
		formatter.printHelp(pw, 80, cmd, "Options:", options, 2, 4, "", false);
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append(sw.getBuffer());
		sb.append("\n");
		System.out.println(sb.toString());
		return CMD_SUCCESS;
	}
}
