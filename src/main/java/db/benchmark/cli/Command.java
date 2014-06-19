package db.benchmark.cli;

public interface Command {
	public static final String CMD_SUCCESS = "success";
	public static final String CMD_FAILURE = "failure";
	
	public String execute();

}
