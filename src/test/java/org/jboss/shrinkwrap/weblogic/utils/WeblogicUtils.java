package org.jboss.shrinkwrap.weblogic.utils;

import org.jboss.arquillian.container.wls.ShrinkWrapUtil;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.weblogic.api.WebLogicEnterpriseArchive;
import org.jboss.shrinkwrap.weblogic.api.WebLogicJavaArchive;
import org.jboss.shrinkwrap.weblogic.api.WebLogicWebArchive;
import weblogic.management.DeploymentException;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.fail;

/**
 * Utils class which is used to handle connections to the WebLogic server using JMX, deploy/undeploy the archives, find
 * all archives and libraries deployed on the server.
 *
 * @author Noah Arliss, Jyotishman Nag
 */
public class WeblogicUtils {

    /**
     * {@link javax.management.MBeanServerConnection} used to establish a connection with the WebLogic server
     */
    private static MBeanServerConnection connection;
    /**
     * {@link javax.management.remote.JMXConnector} used to connect to the WebLogic server using JMX
     */
    private static JMXConnector connector;
    /**
     * Server where the archives will be deployed
     */
    private static String target;

    /**
     * Establishes a connection to the WebLogic server using JMX
     *
     * @param webLogicProperties - Properties map which was loaded from the ShrinkWrapWeblogicTest.properties file
     */
    public static void initConnection(Properties webLogicProperties) {
        Integer portInteger = Integer.valueOf(webLogicProperties.getProperty(WeblogicPropertiesConstants.PORT_STRING));
        int port = portInteger.intValue();
        String jndiroot = "/jndi/";
        String mserver = "weblogic.management.mbeanservers.domainruntime";

        try {
            JMXServiceURL serviceURL =
                    new JMXServiceURL(webLogicProperties.getProperty(WeblogicPropertiesConstants.PROTOCOL),
                            webLogicProperties.getProperty(WeblogicPropertiesConstants.HOSTNAME), port,
                            jndiroot + mserver);

            Hashtable h = new Hashtable();
            h.put(Context.SECURITY_PRINCIPAL, webLogicProperties.getProperty(WeblogicPropertiesConstants.USERNAME));
            h.put(Context.SECURITY_CREDENTIALS, webLogicProperties.getProperty(WeblogicPropertiesConstants.PASSWORD));
            h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, "weblogic.management.remote");

            connector = JMXConnectorFactory.connect(serviceURL, h);
            connection = connector.getMBeanServerConnection();
            target = webLogicProperties.getProperty(WeblogicPropertiesConstants.TARGET);
        } catch (MalformedURLException e) {
            fail("Unable to create a connection " + e.getLocalizedMessage());
        } catch (IOException e) {
            fail("Unable to create a connection " + e.getLocalizedMessage());
        }

    }

    /**
     * Used to find a list of all deployments on the WebLogic server of a given type passed in as the param.
     *
     * @param type
     * @return
     */
    private static List<String> findDeployments(String type) {
        try {
            ObjectName domainRuntimeService = new ObjectName(
                    "com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers." +
                            "domainruntime.DomainRuntimeServiceMBean");

            ObjectName domainConfig = (ObjectName) connection.getAttribute(domainRuntimeService, "DomainConfiguration");
            ObjectName[] deployments = (ObjectName[]) connection.getAttribute(domainConfig, type);

            List<String> deploymentsList = new ArrayList<String>();

            for (ObjectName deployment : deployments) {
                String libraryDeployment = (String) connection.getAttribute(deployment, "Name");
                deploymentsList.add(libraryDeployment);
            }

            return deploymentsList;
        } catch (Exception e) {
            fail("Could not find deployed apps, Cause:" + e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * @return - List of all applications deployed in the WebLogic server
     */
    public static List<String> findAllAppDeployments() {
        return findDeployments("AppDeployments");
    }

    /**
     * @return - List of all libraries deployed in the WebLogic server
     */
    public static List<String> findAllLibraryDeployments() {
        return findDeployments("Libraries");
    }

    /**
     * Deploys the archive to the WebLogic server using the name provided
     *
     * @param deploymentName - Name of the archive
     * @param archive        - Archive to be deployed
     * @throws DeploymentException
     */
    public static void doDeploy(String deploymentName, Archive<?> archive) throws DeploymentException {
        try {
            ObjectName domainRuntime = null;
            ObjectName domainRuntimeService = new ObjectName(
                    "com.bea:Name=DomainRuntimeService,Type=weblogic.management." +
                            "mbeanservers.domainruntime.DomainRuntimeServiceMBean");

            domainRuntime = (ObjectName) connection.getAttribute(domainRuntimeService, "DomainRuntime");
            ObjectName deploymentManager = (ObjectName) connection.getAttribute(domainRuntime, "DeploymentManager");
            File deploymentArchive = ShrinkWrapUtil.toFile(archive);

            Properties props = new Properties();
            props.setProperty("stageMode", "stage");

            if (archive instanceof WebLogicEnterpriseArchive) {
                WebLogicEnterpriseArchive wlsArchive = (WebLogicEnterpriseArchive) archive;
                props.setProperty("stageMode", wlsArchive.getStageMode().toString());

                if (wlsArchive.isSharedLibrary()) {
                    props.setProperty("library", "true");
                }
            } else if (archive instanceof WebLogicWebArchive) {
                WebLogicWebArchive wlsArchive = (WebLogicWebArchive) archive;
                props.setProperty("stageMode", wlsArchive.getStageMode().toString());

                if (wlsArchive.isSharedLibrary()) {
                    props.setProperty("library", "true");
                }
            } else if (archive instanceof WebLogicJavaArchive) {
                WebLogicJavaArchive wlsArchive = (WebLogicJavaArchive) archive;
                props.setProperty("stageMode", wlsArchive.getStageMode().toString());

                if (wlsArchive.isSharedLibrary()) {
                    props.setProperty("library", "true");
                }
            }

            ObjectName deploymentProgressObject = (ObjectName) connection.invoke(deploymentManager, "deploy",
                    new Object[]{deploymentName, deploymentArchive.getAbsolutePath(), new String[]{target}, null,
                            props},
                    new String[]{String.class.getName(), String.class.getName(), String[].class.getName(),
                            String.class.getName(), Properties.class.getName()});

            processDeploymentProgress(deploymentName, deploymentManager, deploymentProgressObject);
        } catch (DeploymentException e) {
            throw e;
        } catch (Exception e) {
            throw new DeploymentException(e.getMessage(), e);
        }
    }

    /**
     * Undeploys the archive with the deploymentName passed in from the WebLogic server
     *
     * @param deploymentName    - Name of archive to be undeployed
     * @param deploymentArchive - Archive to be undeployed
     * @throws org.jboss.arquillian.container.spi.client.container.DeploymentException
     */
    public static void undeploy(String deploymentName, Archive<?> deploymentArchive)
            throws org.jboss.arquillian.container.spi.client.container.DeploymentException {
        try {
            ObjectName domainRuntime = null;
            ObjectName domainRuntimeService = new ObjectName(
                    "com.bea:Name=DomainRuntimeService,Type=weblogic.management." +
                            "mbeanservers.domainruntime.DomainRuntimeServiceMBean");

            domainRuntime = (ObjectName) connection.getAttribute(domainRuntimeService, "DomainRuntime");
            ObjectName deploymentManager = (ObjectName) connection.getAttribute(domainRuntime, "DeploymentManager");
            ObjectName appDeploymentRuntime;

            if (deploymentArchive instanceof WebLogicEnterpriseArchive &&
                    ((WebLogicEnterpriseArchive) deploymentArchive).isSharedLibrary()) {
                appDeploymentRuntime = (ObjectName) connection
                        .invoke(deploymentManager, "lookupLibDeploymentRuntime", new Object[]{deploymentName},
                                new String[]{String.class.getName()});
            } else if (deploymentArchive instanceof WebLogicJavaArchive &&
                    ((WebLogicJavaArchive) deploymentArchive).isSharedLibrary()) {
                appDeploymentRuntime = (ObjectName) connection
                        .invoke(deploymentManager, "lookupLibDeploymentRuntime", new Object[]{deploymentName},
                                new String[]{String.class.getName()});

            } else if (deploymentArchive instanceof WebLogicWebArchive &&
                    ((WebLogicWebArchive) deploymentArchive).isSharedLibrary()) {
                appDeploymentRuntime = (ObjectName) connection
                        .invoke(deploymentManager, "lookupLibDeploymentRuntime", new Object[]{deploymentName},
                                new String[]{String.class.getName()});

            } else {
                appDeploymentRuntime = (ObjectName) connection
                        .invoke(deploymentManager, "lookupAppDeploymentRuntime", new Object[]{deploymentName},
                                new String[]{String.class.getName()});
            }

            ObjectName deploymentProgressObject =
                    (ObjectName) connection.invoke(appDeploymentRuntime, "undeploy", new Object[]{}, new String[]{});

            processDeploymentProgress(deploymentName, deploymentManager, deploymentProgressObject);
        } catch (org.jboss.arquillian.container.spi.client.container.DeploymentException e) {
            throw e;
        } catch (Exception e) {
            throw new org.jboss.arquillian.container.spi.client.container.DeploymentException(e.getMessage(), e);
        }
    }

    /**
     * Closes the connection to the WebLogic server
     */
    public static void closeConnection() {
        try {
            if (connector != null) {
                connector.close();
            }
        } catch (IOException e) {
            fail("Could not close connection " + e.getLocalizedMessage());
        }
    }

    /**
     * Utility method used to deploy the application to the WebLogic server
     *
     * @param appName
     * @param deploymentManager
     * @param deploymentProgressObject
     * @throws Exception
     */
    private static void processDeploymentProgress(String appName, ObjectName deploymentManager,
                                                  ObjectName deploymentProgressObject) throws Exception {
        if (deploymentProgressObject != null) {
            try {
                String state = waitForDeployToComplete(deploymentProgressObject, 200);
                if (state.equals("STATE_FAILED")) {
                    String[] targets = (String[]) connection.getAttribute(deploymentProgressObject, "FailedTargets");
                    RuntimeException[] exceptions = (RuntimeException[]) connection
                            .invoke(deploymentProgressObject, "getExceptions", new Object[]{targets[0]},
                                    new String[]{String.class.getName()});

                    throw new DeploymentException("Deployment Failed on server: " + exceptions[0].getMessage(),
                            exceptions[0]);
                }
            } finally {
                connection.invoke(deploymentManager, "removeDeploymentProgressObject", new Object[]{appName},
                        new String[]{"java.lang.String"});
            }
        }
    }

    /**
     * Utility method monitoring state of the application being deployed
     *
     * @param progressObj
     * @param timeToWaitInSecs
     * @return
     * @throws Exception
     */
    private static String waitForDeployToComplete(ObjectName progressObj, int timeToWaitInSecs) throws Exception {
        for (int i = 0; i < timeToWaitInSecs; i++) {
            String state = (String) connection.getAttribute(progressObj, "State");
            if ("STATE_COMPLETED".equals(state) || "STATE_FAILED".equals(state)) {
                return state;
            }
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException ex) {
                //ignore
            }
        }
        return "unkown";
    }

}
