package edu.cornell.weill.boe.enterprisetools;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.crystaldecisions.sdk.occa.infostore.IInfoStore;

import edu.cornell.weill.boe.enterprisetools.scripts.BObjUserScript;
import edu.cornell.weill.boe.enterprisetools.scripts.CreateAliases;
import edu.cornell.weill.boe.enterprisetools.scripts.PrintHelpText;

public class ArgumentsParser {

	static Options options = new Options();
	static HelpFormatter formatter = new HelpFormatter();

	static {
		options.addOption("v", false, "Verbose?");
		options.addOption("N", true, "New type of alias to create");
		options.addOption("e", true, "Existing alias authentication type");
		options.addOption("s", false, "Save changes?");

	}

	private final CommandLineParser parser = new BasicParser();

	private CommandLine cmd = null;

	public CommandLine getCmd() {
		return cmd;
	}

	public ArgumentsParser(String[] args) throws ParseException {
		cmd = parser.parse(options, args);
	}

	public BObjUserScript getScript(IInfoStore boInfoStore) throws ArgumentsParseException, ParseException {

		if (cmd.hasOption("help")) {

			return new PrintHelpText(options, formatter);

		} else if (cmd.hasOption('N')) {

			return new CreateAliases(boInfoStore, cmd.hasOption("v"), cmd.hasOption("s"), cmd.getOptionValue("N"),
					cmd.getOptionValue("e"));

		} else {
			throw new ArgumentsParseException("Invalid Arguments! Need to know what to do.");
		}
	}
}
