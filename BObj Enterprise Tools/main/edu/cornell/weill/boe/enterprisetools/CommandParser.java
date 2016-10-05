package edu.cornell.weill.boe.enterprisetools;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import edu.cornell.weill.boe.enterprisetools.scripts.BObjUserScript;
import edu.cornell.weill.boe.enterprisetools.scripts.CreateAliases;
import edu.cornell.weill.boe.enterprisetools.scripts.PrintHelpText;

public class CommandParser {

	static Options options = new Options();
	static HelpFormatter formatter = new HelpFormatter();

	static {
		options.addOption("v", true, "Verbose?");
		options.addOption("N", true, "New type of alias to create");
		options.addOption("e", true, "Existing alias authentication type");
		options.addOption("s", true, "Save changes?");
		options.addOption("help", "print this message");

	}

	public static BObjUserScript parse(String[] args) throws CommandParseException, ParseException {

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);

		if (cmd.hasOption("help")) {

			return new PrintHelpText(options, formatter);

		} else if (cmd.hasOption('N')) {

			return new CreateAliases(cmd.hasOption("v"), cmd.hasOption("s"), cmd.getOptionValue("N"),
					cmd.getOptionValue("e"));

		} else {
			throw new CommandParseException("Invalid Arguments! Need to know what to do.");
		}

	}
}
