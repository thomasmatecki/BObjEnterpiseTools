package edu.cornell.weill.boe.enterprisetools;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.cli.ParseException;

import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.crystaldecisions.sdk.framework.ISessionMgr;
import com.crystaldecisions.sdk.occa.infostore.IInfoObjects;
import com.crystaldecisions.sdk.occa.infostore.IInfoStore;
import com.crystaldecisions.sdk.plugin.desktop.user.IUser;
import com.crystaldecisions.sdk.plugin.desktop.user.IUserAlias;
import com.crystaldecisions.sdk.plugin.desktop.user.IUserAliases;

import edu.cornell.weill.boe.enterprisetools.scripts.BObjUserScript;

public class MassUserUtil implements com.crystaldecisions.sdk.plugin.desktop.program.IProgramBase {

	public static void main(String[] args) {
		/*
		 * The main method is only going to be called when the jar is run from
		 * the command line. If the jar is run a a program file within BI, the
		 * entire main method will be skipped. Keep this in mind when adding
		 * additional code to the main method.
		 */

		IEnterpriseSession boEnterpriseSession = null;
		ISessionMgr boSessionMgr = null;
		IInfoStore boInfoStore = null;
		String userName = null;
		String cmsName = null;
		String password = null;
		String authType = null;

		/* Enforce all expected cmd line arguments have been submitted */
		if ((args.length > 4) && args[0] != null) {
			/* Login Information; Note order of args */

			userName = args[0];
			password = args[1];
			cmsName = args[2];
			authType = args[3];

			try {
				// Initialize the Session Manager
				boSessionMgr = CrystalEnterprise.getSessionMgr();

				// Logon to the Session Manager to create a new BOE session.
				boEnterpriseSession = boSessionMgr.logon(userName, password, cmsName, authType);

				// Retrieve the InfoStore object
				boInfoStore = (IInfoStore) boEnterpriseSession.getService("", "InfoStore");

				// Execute Application Script
				MassUserUtil mau = new MassUserUtil();
				mau.run(boEnterpriseSession, boInfoStore, args);

			} catch (SDKException sdke) {
				System.out.println(sdke.getMessage());
				System.exit(1);
			}
		} else {
			System.out.println("... Invalid Arguments");
		}
	}

	public void run(IEnterpriseSession boEnterpriseSession, IInfoStore boInfoStore, String args[]) throws SDKException {

		InfoStoreQueryHelper<IInfoObjects> isqh;

		try {

			BObjUserScript script = CommandParser.parse(args);

			isqh = new InfoStoreQueryHelper<IInfoObjects>(boInfoStore, "CI_SYSTEMOBJECTS", "SI_KIND='User'");

			script.run(isqh);

		} catch (SDKException sdke) {
			throw sdke;
		} catch (CommandParseException e) {
			throw new SDKException.InvalidArg(e);
		} catch (ParseException e) {
			throw new SDKException.InvalidArg(e);
		}
		System.out.println("Done.");
	}
}
