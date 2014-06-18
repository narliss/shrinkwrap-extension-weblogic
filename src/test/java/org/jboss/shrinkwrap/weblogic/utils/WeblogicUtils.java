package org.jboss.shrinkwrap.weblogic.utils;

import junit.framework.Assert;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.Properties;

/**
 * Created by jnag on 6/12/14.
 */
public class WeblogicUtils {

    private static MBeanServerConnection connection;
    private static JMXConnector connector;



    public static void initConnection(Properties webLogicProperties)
    {
        try{
        Integer portInteger = Integer.valueOf(webLogicProperties.getProperty(WeblogicConstants.PORT_STRING));
        int port = portInteger.intValue();
        String jndiroot = "/jndi/";
        String mserver = "weblogic.management.mbeanservers.domainruntime";
        JMXServiceURL serviceURL = new JMXServiceURL(webLogicProperties.getProperty(WeblogicConstants.PROTOCOL), webLogicProperties.getProperty(WeblogicConstants.HOSTNAME), port,
                jndiroot + mserver);
        Hashtable h = new Hashtable();
        h.put(Context.SECURITY_PRINCIPAL, webLogicProperties.getProperty(WeblogicConstants.USERNAME));
        h.put(Context.SECURITY_CREDENTIALS, webLogicProperties.getProperty(WeblogicConstants.PASSWORD));
        h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES,
                "weblogic.management.remote");
        connector = JMXConnectorFactory.connect(serviceURL, h);
        connection = connector.getMBeanServerConnection();
    }
        catch(MalformedURLException e)
        {
            Assert.fail("Unable to create a connection "+ e.getLocalizedMessage());
        } catch (IOException e) {
            Assert.fail("Unable to create a connection " + e.getLocalizedMessage());
        }

    }



    public static ObjectName[] findAllAppDeployments()
    {
        try {
            ObjectName domainRuntimeService = new ObjectName("com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean");

            ObjectName domainConfig = (ObjectName) connection.getAttribute(domainRuntimeService, "DomainConfiguration");
            ObjectName[] appDeployments = (ObjectName[]) connection.getAttribute(domainConfig, "AppDeployments");
            ObjectName[] libraries = (ObjectName[]) connection.getAttribute(domainConfig, "Libraries");

            for (ObjectName appDeployment : appDeployments) {
                String appDeploymentName = (String) connection.getAttribute(appDeployment, "Name");
                System.out.println(appDeploymentName);
            }

            for (ObjectName library : libraries) {
                String appDeploymentName = (String) connection.getAttribute(library, "Name");
                System.out.println(appDeploymentName);
            }

            System.out.println();
            return appDeployments;

        }
        catch(Exception e){
            Assert.fail("Could not find deployed apps, Cause:" +e.getLocalizedMessage());
            return null;
        }
    }

}
