package db.benchmark.cli;

import db.benchmark.cli.bi.InsertCommand;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommandParserTest {
	
	private CommandParser parser;
	
	@Before
	public void setUp() {
		parser = new CommandParser();
	}
	
	@After
	public void tearDown() {
		parser = null;
	}

	@Test
	public void testParse() {
		Command cmd = null;
		String[] args = null;
		
//		args = new String[] {};
//		cmd = parser.parse(args);
//		assertTrue(cmd instanceof HelpCommand);
//		System.out.println(cmd.execute());
//		
//		args = new String[] {"-h"};
//		cmd = parser.parse(args);
//		assertTrue(cmd instanceof HelpCommand);
//		
//		args = new String[] {"-v"};
//		cmd = parser.parse(args);
//		assertTrue(cmd instanceof VersionCommand);
//		
//		args = new String[] {"-insert", "100", "10", "5", "5"};
//		cmd = parser.parse(args);
//		assertTrue(cmd instanceof InsertCommand);
//		//cmd.execute();
	}
}
