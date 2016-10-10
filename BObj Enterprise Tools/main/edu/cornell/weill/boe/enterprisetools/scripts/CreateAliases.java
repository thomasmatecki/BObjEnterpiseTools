package edu.cornell.weill.boe.enterprisetools.scripts;

import java.util.Iterator;

import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.occa.infostore.IInfoObjects;
import com.crystaldecisions.sdk.occa.infostore.IInfoStore;
import com.crystaldecisions.sdk.plugin.desktop.user.IUser;
import com.crystaldecisions.sdk.plugin.desktop.user.IUserAlias;
import com.crystaldecisions.sdk.plugin.desktop.user.IUserAliases;

import edu.cornell.weill.boe.enterprisetools.InfoStoreQueryHelper;

public class CreateAliases implements BObjUserScript {

	private final boolean verbose;
	private final boolean saveChanges;
	private final String existingAliasAuthentication;
	private final String newAliasAuthentication;
	private final InfoStoreQueryHelper<IInfoObjects> isqh;

	public CreateAliases(IInfoStore boInfoStore, boolean verbose, boolean saveChanges, String newAuth,
			String existingAuth) {
		
		this.verbose = verbose;
		this.saveChanges = saveChanges;
		this.newAliasAuthentication = newAuth;
		this.existingAliasAuthentication = existingAuth;
		this.isqh = new InfoStoreQueryHelper<IInfoObjects>(boInfoStore, "CI_SYSTEMOBJECTS", "SI_KIND='User'");
	
	}

	public void run() throws SDKException {

		while (isqh.hasNext()) {

			IUser user = (IUser) isqh.next();

			if (verbose) {
				System.out.print("\nUser " + user.getTitle());
			}

			boolean hasExisting = false;
			boolean hasNew = false;

			IUserAliases userAliases = user.getAliases();
			Iterator<IUserAlias> ialias = userAliases.iterator();

			if (verbose) {
				System.out.print(" has aliases:");
			}

			while (ialias.hasNext()) {
				IUserAlias userAlias = ialias.next();
				String authentication = userAlias.getAuthentication();
				if (verbose) {
					System.out.print("\n\t" + userAlias.getID() + " / " + authentication
							+ (userAlias.isDisabled() ? "(disabled)" : ""));
				}

				hasExisting = existingAliasAuthentication.equals(authentication) ? true : hasExisting;
				hasNew = (newAliasAuthentication.equals(authentication)) ? true : hasNew;
			}

			if (saveChanges && !hasNew && hasExisting) {

				String newAliasName = "newAliasAuthentication:" + user.getTitle();

				System.out.println("\n\t ***Adding Alias " + newAliasName + "***");

				IUserAlias newAlias = userAliases.addNew(newAliasName, true);
				user.setNewPassword("Password123");
				newAlias.setDisabled(true);
			}

			if (user.isDirty()) {
				if (saveChanges) {
					System.out.println("\t ***Saving*** ");
					user.save();
				} else {
					System.out.println("\t ***Changes not saved. Add flag -s to save changes***");
				}
			}
		}
	}
}
