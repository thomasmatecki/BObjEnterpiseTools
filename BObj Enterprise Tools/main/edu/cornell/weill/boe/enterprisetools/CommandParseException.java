package edu.cornell.weill.boe.enterprisetools;

@SuppressWarnings("serial")
public class CommandParseException extends Exception {

	public CommandParseException(String string) {
		super("Error Parsing Arguments: " + string);
	}
}
