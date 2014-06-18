package org.jboss.shrinkwrap.weblogic.tests;

import org.jboss.shrinkwrap.weblogic.api.WebLogicEnterpriseArchive;
import org.jboss.shrinkwrap.weblogic.api.WebLogicJavaArchive;
import org.jboss.shrinkwrap.weblogic.api.WebLogicWebArchive;

import java.util.Properties;

/**
 * Variables definitions being used in {@link org.jboss.shrinkwrap.weblogic.tests} package.
 *
 * @author Jyotishman Nag
 */
public class SharedArchiveTest {

    /**
     * Constant defining name of {@link #dependentWebArchive}
     */
    protected static final String dependentWarName = "DependentWar";
    /**
     * Constant defining context root of {@link #dependentWebArchive} war
     */
    protected static final String dependentWarContextRoot = "/" + dependentWarName;
    /**
     * Constant defining name of the {@link #sharedJavaArchive}
     */
    protected static final String sharedJarName = "SharedJar";
    /**
     * Constant defining the name of the {@link #dependentEnterpriseArchive}.
     */
    protected static final String dependentEarName = "DependentEar";
    /**
     * Constant defining the name of the {@link #sharedWebArchive}
     */
    protected static final String sharedWarName = "SharedWar";
    /**
     * Constant defining name of {@link #sharedEnterpriseArchive}
     */
    protected static final String sharedEarName = "SharedEar";
    /**
     * Jar file which is going to be deployed as a shared library in the weblogic server.
     */
    protected static WebLogicJavaArchive sharedJavaArchive;
    /**
     * Ear file deployed in the WebLogic server, which will have dependencies on other libraries deployed in the
     * server.
     */
    protected static WebLogicEnterpriseArchive dependentEnterpriseArchive;
    /**
     * War file deployed in the WebLogic server, which will have dependencies on other libraries deployed in the
     * server.
     */
    protected static WebLogicWebArchive dependentWebArchive;
    /**
     * The ShrinkWrapWeblogicTest.properties file is loaded into this properties map.
     */
    protected static Properties webLogicProperties;
    /**
     * War file which is going to be deployed as a shared library in the WebLogic server.
     */
    protected static WebLogicWebArchive sharedWebArchive;
    /**
     * Ear file which will be deployed as a shared library
     */
    protected static WebLogicEnterpriseArchive sharedEnterpriseArchive;
}
