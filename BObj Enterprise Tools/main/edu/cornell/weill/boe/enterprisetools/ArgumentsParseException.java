package edu.cornell.weill.boe.enterprisetools;

@SuppressWarnings("serial")
public class ArgumentsParseException extends Exception {

	public ArgumentsParseException(String string) {
		super("Error Parsing Arguments: " + string);
	}
}
