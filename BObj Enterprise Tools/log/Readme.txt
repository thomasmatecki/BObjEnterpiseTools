BObj Enterprise Tools 
Main Class: edu.cornell.weill.boe.enterprisetools.MassAliasUtil(this should be in the jar manifest)

§ Change Log:
  v1.0(04/29/2016) - Initial creation. Compiled using: JavaSE-1.6(jdk1.6.0_45) and tested on SAP 
                     BusinessObjects BI Platform 4.1 Support Pack 6 Patch 2

§ How To:
  When executing, arguments are passed to affect WHAT the utility will do(see 'Flags' below). Additionally 
  if it is being run locally(not on a BOBJ server), then arguments must be passed allowing the tool to 
  connect and authenticate to a Business Objects Server.  On a bobj server, logon is handled externally, so 
  only need to provide flags telling the utlity what to do.
  
    Connecting and Authenticating:
    Arguments Must be passed as follows:
    <username> <password> <host:port> <authentication mechanism>
    
    Flags:
    -E :Add missing enterprise aliases; If a user has an SAP alias, but no enterprise alias, the enterprise
        alias is added.
        
    -s :Save Changes; without this flag, no changes are saved. A message indicated any changes is added to 
        the output file, but it is accompanied with a message that the changes will not be saved.
        
    -v :Verbose; Additional information is added to the output file. Probably don’t want to have this enabled 
        when the tool is scheduled.
  
  Example:
  To Run Locally, execute: java -jar MassAliasUtil.jar username mypassword bobjserver.com:6400 secEnterprise -s -E
  

