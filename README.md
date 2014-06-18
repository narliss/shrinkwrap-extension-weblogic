## Overview

Shrinkwrap extension to support WebLogic deployment and shared library information. WebLogic Archives have the added knowledge of how they should be staged or whether they should be deployed as a shared library.

## Prerequisites for integration tests:

* You need to have Weblogic 12c installed on your machine.
* Maven sync plugin


## Setting up the environment for integration tests:

* You would need Weblogic 12c installed on your machine to proceed with the integration tests. After you have it installed you'll need to install the Maven Sync Plugin. We'll refer to your installation dir as $ORACLE_HOME.


* Install maven sync plugin:


	cd $ORACLE_HOME\oracle_common\plugins\maven\com\oracle\maven\oracle-maven-sync\12.1.2       
    
	mvn install:install-file -DpomFile=oracle-maven-sync.12.1.2.pom -Dfile=oracle-maven-sync.12.1.2.jar 

	mvn com.oracle.maven:oracle-maven-sync:push -Doracle-maven-sync.oracleHome=$ORACLE_HOME


* Edit the following files: 
    
    * pom.xml: 
    
    Modify the following properties: 
    oracle.home         : Property to point to your local Weblogic installation.
    weblogic.version    : Your version of Weblogic
    username , password : Login credentials to your weblogic server
    domain.home:        : You can choose to use an existing domain, or if you go with the one specified it will create a new domain called shrinkwrap-extension-weblogic and use it for the purpose of these tests.
	
    * ShrinkWrapWeblogicTest.properties under shrinkwrap-extension-weblogic/src/test/resources.
    
    Modify the credentials accordingly.
     

## Building the extension:

The project can be built using maven by running: 

    mvn clean install

This would run some integration tests for which you'll need to set up your environment according to the notes below. 

If you want to skip the tests, you can simply run:

    mvn clean package.



    



