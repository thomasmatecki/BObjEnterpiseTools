package edu.cornell.weill.boe.enterprisetools;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.crystaldecisions.sdk.framework.ISessionMgr;
import com.crystaldecisions.sdk.occa.infostore.IInfoObjects;
import com.crystaldecisions.sdk.occa.infostore.IInfoStore;
import com.crystaldecisions.sdk.plugin.desktop.user.IUser;
import com.crystaldecisions.sdk.plugin.desktop.user.IUserAlias;
import com.crystaldecisions.sdk.plugin.desktop.user.IUserAliases;

public class MassAliasUtil implements com.crystaldecisions.sdk.plugin.desktop.program.IProgramBase {

  public static void main(String[] args) {
    /*
     * The main method is only going to be called when the jar is run from the
     * command line. If the jar is run a a program file within BI, the entire
     * main method will be skipped. Keep this in mind when adding additional
     * code to the main method.
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
      /* Logon Information; Note order of args */

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
        MassAliasUtil mau = new MassAliasUtil();
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
    /*
     * The run() method is called directly when run as a program file within BI.
     * The enterprisesession and infostore objects are automatically passed to
     * it at runtime as well as any cmd line arguments
     */

    List<String> argsList = Arrays.asList(args);
    boolean verbose = false, addEnterprise = false, saveChanges = false;
    InfoStoreQueryHelper<IInfoObjects> isqh;

    /*
     * Argument '-v' : "verbose"; additional logging takes place.
     */
    if (argsList.contains("-v")) {
      verbose = true;
      System.out.println("User \"" + boEnterpriseSession.getUserInfo().getUserName() + "\" logged in");
    }

    /*
     * Argument '-E' : Create an enterprise Alias for each user that has an
     * SAPR3 alias, but no enterprise alias.
     */
    if (argsList.contains("-E")) {
      addEnterprise = true;
    }

    /*
     * Argument '-s' : "Save Changes"; Call method save() for each user being
     * updated.
     */
    if (argsList.contains("-s")) {
      saveChanges = true;
    }

    try {

      isqh = new InfoStoreQueryHelper<IInfoObjects>(boInfoStore, "CI_SYSTEMOBJECTS", "SI_KIND='User'");

      while (isqh.hasNext()) {

        IUser user = (IUser) isqh.next();

        if (verbose) {
          System.out.print("\nUser " + user.getTitle());
        }

        boolean hasSAPR3 = false;
        boolean hasEnterprise = false;

        IUserAliases userAliases = user.getAliases();
        Iterator<IUserAlias> ialias = userAliases.iterator();

        if (verbose) {
          System.out.print(" has aliases:");
        }

        while (ialias.hasNext()) {
          IUserAlias userAlias = ialias.next();
          String authentication = userAlias.getAuthentication();
          if (verbose) {
            System.out.print("\n\t" + userAlias.getID() + " / " + authentication);
          }
          hasSAPR3 = "secSAPR3".equals(authentication) ? true : hasSAPR3;
          hasEnterprise = ("secEnterprise".equals(authentication)) ? true : hasEnterprise;
        }

        if (addEnterprise && !hasEnterprise && hasSAPR3) {

          String newAliasName = "secEnterprise:" + user.getTitle();

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
    } catch (SDKException sdke) {
      throw sdke;
    }
    System.out.println("Done.");
  }
}
