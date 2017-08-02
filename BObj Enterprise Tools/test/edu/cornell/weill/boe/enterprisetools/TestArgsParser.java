package edu.cornell.weill.boe.enterprisetools;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.CommandLine;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestArgsParser {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@Test
	public void testNewAliasWithoutSave() {

		String[] args = { "-N", "secSAPR3", "-e", "secEnterprise", "-v" };

		try {
			ArgumentsParser argsParser = new ArgumentsParser(args);

			CommandLine cmd = argsParser.getCmd();

			assertEquals(cmd.getOptionValue("N"), "secSAPR3");
			assertEquals(cmd.getOptionValue("e"), "secEnterprise");
			assertTrue(cmd.hasOption("v"));
			assertFalse(cmd.hasOption("s"));

		} catch (ParseException e) {
			fail("Parser Exception Occurred: " + e.getMessage());
		}
	}


}
