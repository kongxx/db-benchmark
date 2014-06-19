package db.benchmark.cli;

public class VersionCommand implements Command {

	@Override
	public String execute() {
		System.out.println("b version 1.0.0\n");
		return CMD_SUCCESS;
	}
}
