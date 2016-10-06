package edu.cornell.weill.boe.enterprisetools;

import org.apache.commons.cli.ParseException;

import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.crystaldecisions.sdk.framework.ISessionMgr;
import com.crystaldecisions.sdk.occa.infostore.IInfoStore;

import edu.cornell.weill.boe.enterprisetools.scripts.BObjUserScript;

public class MassUserUtil implements com.crystaldecisions.sdk.plugin.desktop.program.IProgramBase {

	public static void main(String[] args) {
		/*
		 * The main method is only going to be called when the jar is run from
		 * the command line. If the jar is run a a program file within BI, the
		 * entire main method will be skipped. Keep this in mind when adding
		 * additional code to the main method. Also note the hacky parsing of
		 * the command line arguments needed to obtain a connection to a remote
		 * BObj CMS.
		 */

		IEnterpriseSession boEnterpriseSession = null;
		ISessionMgr boSessionMgr = null;
		IInfoStore boInfoStore = null;
		String userName = null;
		String cmsName = null;
		String password = null;
		String authType = null;
		try {
			if ((args.length > 4) && args[0] != null) {
				/* Login Information; Note order of args */

				userName = args[0];
				password = args[1];
				cmsName = args[2];
				authType = args[3];
			} else {
				throw new ArgumentsParseException("Invalid arguments passed for connection to remote CMS");
			}

			// Initialize the Session Manager
			boSessionMgr = CrystalEnterprise.getSessionMgr();

			// Logon to the Session Manager to create a new BOE session.
			boEnterpriseSession = boSessionMgr.logon(userName, password, cmsName, authType);

			// Retrieve the InfoStore object
			boInfoStore = (IInfoStore) boEnterpriseSession.getService("", "InfoStore");

		} catch (SDKException sdke) {
			System.out.println(sdke.getMessage());
		} catch (ArgumentsParseException ape) {
			System.out.println(ape.getMessage());
		} finally {

			/*
			 * Execute Application Script. Do this even if an exception has
			 * occurred when parsing the name and credentials of the remote BObj
			 * CMS. Some Scripts(e.g. the help output) do not require a
			 * connection. For those that do, the failure in the script
			 * resulting from the lack of a connection is just more information
			 * for the user.
			 */

			try {
				MassUserUtil mau = new MassUserUtil();
				mau.run(boEnterpriseSession, boInfoStore, args);
			} catch (SDKException sdke) {
				System.out.println(sdke.getMessage());
				System.exit(1);
			}
		}

	}

	public void run(IEnterpriseSession boEnterpriseSession, IInfoStore boInfoStore, String[] args) throws SDKException {

		try {

			ArgumentsParser argsParser = new ArgumentsParser(args);

			BObjUserScript script = argsParser.getScript(boInfoStore);

			script.run();

		} catch (SDKException sdke) {
			throw sdke;
		} catch (ArgumentsParseException e) {
			throw new SDKException.InvalidArg(e);
		} catch (ParseException e) {
			throw new SDKException.InvalidArg(e);
		}
		
		System.out.println();
		System.out.println("Done.");
	}
}
