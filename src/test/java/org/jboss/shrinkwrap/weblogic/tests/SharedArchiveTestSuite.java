package org.jboss.shrinkwrap.weblogic.tests;

import org.jboss.shrinkwrap.weblogic.utils.ShrinkWrapTestUtils;
import org.jboss.shrinkwrap.weblogic.utils.WeblogicUtils;
import org.junit.ClassRule;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import static org.junit.Assert.fail;

/**
 * The integration test needs to connect to a running Weblogic server to deploy the archives. The README.md document
 * contains details of how to run this unit test. You would need to edit the ShrinkWrapWeblogicTest.properties file
 * under the resources folder with details about how to connect to your weblogic server (host, port, credentials,
 * target, protocol). You would also need to edit the pom.xml to specify the location of your Weblogic installation
 * ($WEBLOGIC_HOME).
 *
 * @author Jyotishman Nag
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({SharedJarTest.class, SharedWarTest.class, SharedEarTest.class})
public class SharedArchiveTestSuite extends SharedArchiveTest {

    @ClassRule
    public static ExternalResource shrinkWrapWeblogicExtensionRule = new ExternalResource() {
        /**
         * Loads the properties defined in the ShrinkWrapWeblogicTest.properties file into a properties map:
         * {@link org.jboss.shrinkwrap.weblogic.tests.SharedArchiveTest#webLogicProperties}
         */
        @Override
        public void before() {
            webLogicProperties = ShrinkWrapTestUtils.loadPropertiesFile();
            if (webLogicProperties == null) {
                fail("Unable to load ShrinkWrapWeblogicTest.properties file");
            }

            WeblogicUtils.initConnection(webLogicProperties);

        }

        /**
         * Closes the connection with the local weblogic service using the
         * {@link org.jboss.shrinkwrap.weblogic.utils.WeblogicUtils#closeConnection()} method
         */
        @Override
        public void after() {
            try {
                WeblogicUtils.closeConnection();
            } catch (Exception e) {
                fail("Could not close connection");
            }

        }
    };
}
