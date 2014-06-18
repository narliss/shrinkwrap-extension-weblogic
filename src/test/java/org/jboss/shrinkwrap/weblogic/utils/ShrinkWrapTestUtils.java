package org.jboss.shrinkwrap.weblogic.utils;

import junit.framework.Assert;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by jnag on 6/12/14.
 */
public class ShrinkWrapTestUtils {


    public static Properties loadPropertiesFile(){

        try {

            Properties webLogicProperties;
            InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("ShrinkWrapWeblogicTest.properties");
            webLogicProperties = new Properties();
            webLogicProperties.load(in);

            validatePropertiesFile(webLogicProperties);

            return webLogicProperties;


        } catch (Exception e) {
            Assert.fail("Unable to load ShrinkWrapWeblogicTest.properties");
            return null;
        }


    }


    private static void validatePropertiesFile(Properties webLogicProperties){

        if(!webLogicProperties.containsKey(WeblogicConstants.HOSTNAME)){
            Assert.fail("The ShrinkWrapWeblogicTest.properties is missing the hostname field");
        }
        if(!webLogicProperties.containsKey(WeblogicConstants.PORT_STRING)){
            Assert.fail("The ShrinkWrapWeblogicTest.properties is missing the portString field");
        }
        if(!webLogicProperties.containsKey(WeblogicConstants.USERNAME)){
            Assert.fail("The ShrinkWrapWeblogicTest.properties is missing the username field");
        }
        if(!webLogicProperties.containsKey(WeblogicConstants.PASSWORD)){
            Assert.fail("The ShrinkWrapWeblogicTest.properties is missing the password field");
        }
        if(!webLogicProperties.containsKey(WeblogicConstants.TARGET)){
            Assert.fail("The ShrinkWrapWeblogicTest.properties is missing the target target");
        }
        if(!webLogicProperties.containsKey(WeblogicConstants.PROTOCOL)){
            Assert.fail("The ShrinkWrapWeblogicTest.properties is missing the protocol target");
        }


    }

}
