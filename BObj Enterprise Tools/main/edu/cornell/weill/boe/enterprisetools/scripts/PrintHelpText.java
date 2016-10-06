package edu.cornell.weill.boe.enterprisetools.scripts;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import com.crystaldecisions.sdk.exception.SDKException;

public class PrintHelpText implements BObjUserScript {

	private Options options;
	private HelpFormatter formatter;

	public PrintHelpText(Options options, HelpFormatter formatter) {
		this.options = options;
		this.formatter = formatter;
	}

	@Override
	public void run() throws SDKException {
		formatter.printHelp("BObj User Tools", options);
	}
}
