package org.jboss.shrinkwrap.weblogic.utils;

import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.fail;

/**
 * Utility class used to load the properties defined in the ShrinkWrapWeblogicTest.properties file into a properties
 * map.
 * @author Jyotishman Nag
 */
public class ShrinkWrapTestUtils {

    /**
     * Loads the properties from  ShrinkWrapWeblogicTest.properties into a {@link java.util.Properties properties}
     * properties map.
     * @return  {@link java.util.Properties properties}
     */
    public static Properties loadPropertiesFile() {
        try {
            Properties webLogicProperties;
            InputStream in = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("ShrinkWrapWeblogicTest.properties");
            webLogicProperties = new Properties();
            webLogicProperties.load(in);

            validatePropertiesFile(webLogicProperties);

            return webLogicProperties;
        } catch (Exception e) {
            fail("Unable to load ShrinkWrapWeblogicTest.properties");
            return null;
        }
    }

    /**
     * Validates that all the required properties were present in the ShrinkWrapWeblogicTest.properties file. The
     * absence of a property would result in the unit tests not being executed.
     * @param webLogicProperties
     */
    private static void validatePropertiesFile(Properties webLogicProperties) {
        if (!webLogicProperties.containsKey(WeblogicPropertiesConstants.HOSTNAME)) {
            fail("The ShrinkWrapWeblogicTest.properties is missing the hostname field");
        }
        if (!webLogicProperties.containsKey(WeblogicPropertiesConstants.PORT_STRING)) {
            fail("The ShrinkWrapWeblogicTest.properties is missing the portString field");
        }
        if (!webLogicProperties.containsKey(WeblogicPropertiesConstants.USERNAME)) {
            fail("The ShrinkWrapWeblogicTest.properties is missing the username field");
        }
        if (!webLogicProperties.containsKey(WeblogicPropertiesConstants.PASSWORD)) {
            fail("The ShrinkWrapWeblogicTest.properties is missing the password field");
        }
        if (!webLogicProperties.containsKey(WeblogicPropertiesConstants.TARGET)) {
            fail("The ShrinkWrapWeblogicTest.properties is missing the target target");
        }
        if (!webLogicProperties.containsKey(WeblogicPropertiesConstants.PROTOCOL)) {
            fail("The ShrinkWrapWeblogicTest.properties is missing the protocol target");
        }
    }
}
