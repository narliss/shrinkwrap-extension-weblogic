package org.jboss.shrinkwrap.weblogic.tests;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.weblogic.api.WebLogicEnterpriseArchive;
import org.jboss.shrinkwrap.weblogic.api.WebLogicJavaArchive;
import org.jboss.shrinkwrap.weblogic.api.WebLogicWebArchive;
import org.jboss.shrinkwrap.weblogic.sharedjar.samplejar.SampleInterface;
import org.jboss.shrinkwrap.weblogic.sharedjar.samplejar.SampleInterfaceImpl;
import org.jboss.shrinkwrap.weblogic.utils.ShrinkWrapTestUtils;
import org.jboss.shrinkwrap.weblogic.utils.WeblogicConstants;
import org.jboss.shrinkwrap.weblogic.utils.WeblogicUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.jboss.shrinkwrap.weblogic.sharedjar.samplewar.SampleWS;

import java.io.File;
import java.util.Properties;

import weblogic.management.DeploymentException;

import static org.junit.Assert.*;

/**
 * Sample integration test that demonstrates creating a shared library using shrinkwrap, deploying it
 * in Weblogic, and accessing the shared library from a web application (created using shrinkwrap too),
 * deployed in Weblogics.
 * <p/>
 * The integration test needs to connect to a running Weblogic server to deploy the archives.
 * The README.md document contains details of how to run this unit test. You would need to edit the
 * ShrinkWrapWeblogicTest.properties file under the resources folder with details about how to connect to
 * your weblogic server (host, port, credentials, target, protocol).
 * You would also need to edit the pom.xml to specify the location of your Weblogic installation ($WEBLOGIC_HOME).
 *
 * @author Noah Arliss, Jyotishman Nag
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SharedJarTest {

    /**
     * Constant defining name of the war file which will be created. This war will be packed
     * inside the ear and deployed to the Weblogic server.
     */
    private static final String warName = "SampleWar";

    /**
     * Constant defining name of the jar file which will be created, and deployed as a shared library
     * on the WebLogic server.
     */
    private static final String jarName = "SampleJar";

    /**
     * Constant defining the name of ear which will be deployed to the Weblogic server.
     */
    private static final String earName = "SampleEar";

    /**
     * The ShrinkWrapWeblogicTest.properties file is loaded into this properties map.
     */
    private static Properties webLogicProperties;

    /**
     * The jar file which is going to be deployed as a shared library in the weblogic server.
     */
    private static WebLogicJavaArchive javaArchive;

    /**
     * The ear file which is going to be deployed in the Weblogic server
     */
    private static WebLogicEnterpriseArchive enterpriseArchive;



    /**
     * Loads the ShrinkWrapWeblogicTest.properties using the
     * {@link org.jboss.shrinkwrap.weblogic.utils.ShrinkWrapTestUtils#loadPropertiesFile()} method.
     * <p/>
     * Establishes a connection to the Weblogic server using the
     * {@link org.jboss.shrinkwrap.weblogic.utils.WeblogicUtils#initConnection(java.util.Properties)}
     * <p/>
     * This is method is executed before all the tests are run.
     */
    @BeforeClass
    public static void setUp() {

        webLogicProperties = ShrinkWrapTestUtils.loadPropertiesFile();
        if (webLogicProperties == null) {
            fail("Unable to load ShrinkWrapWeblogicTest.properties file");
        }
//
//
//        WeblogicUtils.initConnection(webLogicProperties);
//

    }

    /**
     * Undeploys the ear and jar archives that were deployed on the Weblogic server using the
     * {@link org.jboss.shrinkwrap.weblogic.utils.WeblogicUtils#undeploy(String, org.jboss.shrinkwrap.api.Archive)} method.
     * <p/>
     * Closes the connection to the Weblogic server using the
     * {@link org.jboss.shrinkwrap.weblogic.utils.WeblogicUtils#close()} method.
     * <p/>
     * This method is executed after all the tests have been completed.
     */
    @AfterClass
    public static void tearDown() {

        try {
            WeblogicUtils.undeploy(earName, enterpriseArchive);
            WeblogicUtils.undeploy(jarName, javaArchive);


//            WeblogicUtils.close();

        } catch (Exception e) {
            fail("Unable to undeploy files " + e.getLocalizedMessage());
        }

    }

    /**
     * This test is executed first. We annotated the class with the test order, so that tests are run in ascending alphabetical
     * order based on their method signatures.
     * <p/>
     * The jar is created using the {@link #createJavaArchive()} method and deployed to the Weblogic server using the
     * {@link org.jboss.shrinkwrap.weblogic.utils.WeblogicUtils#doDeploy(String, org.jboss.shrinkwrap.api.Archive)}
     * utility method.
     * <p/>
     * Similarly the ear is created using the {@link #createEnterpriseArchive()} method and deployed to the Weblogic server using the
     * {@link org.jboss.shrinkwrap.weblogic.utils.WeblogicUtils#doDeploy(String, org.jboss.shrinkwrap.api.Archive)}
     * utility method.
     * <p/>
     * Following the deployments, we use the {@link org.jboss.shrinkwrap.weblogic.utils.WeblogicUtils#findAllLibraryDeployments()}
     * util method to get a list of all libraries which have been deployed on the server. We assert to ensure that the jar we just
     * deployed is present in this list.
     * <p/>
     * We use the {@link org.jboss.shrinkwrap.weblogic.utils.WeblogicUtils#findAllAppDeployments()} util method to get a list of all the
     * applications that have been deployed on the server, and assert that the ear we just deployed is present in the list.
     */

   @Test
    public void testDeploy() {

        try {
            createJavaArchive();
            createEnterpriseArchive();

            WeblogicUtils.doDeploy(jarName, javaArchive);

            WeblogicUtils.doDeploy(earName, enterpriseArchive);

            assertTrue(WeblogicUtils.findAllLibraryDeployments().contains(jarName));

            assertTrue(WeblogicUtils.findAllAppDeployments().contains(earName));

        } catch (Exception e) {

            fail("Unable to carry out deployment of files" + e.getLocalizedMessage());
        }
    }


    /**
     * This test is executed following the completion of the {@link #testDeploy()} method. A war file with REST endpoint is packaged
     * as part of the ear that was deployed. The endpoint instantiates an object of a class present in the shared jar, and uses
     * the class to return a string on a GET request.
     *
     * We do a GET request on the endpoint and assert that the value returned matches the string from the war file.
     */
    @Test
    public void testWebService() {

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);

        WebResource service = client.resource("http://" + webLogicProperties.getProperty(WeblogicConstants.HOSTNAME)
                + ":" + webLogicProperties.getProperty(WeblogicConstants.PORT_STRING) + "/sampleWS/rest/sampleWS");

        assertEquals(SampleWS.GREETING, service.get(String.class));

    }


    /**
     *
     */
    private void createJavaArchive() {

        javaArchive = ShrinkWrap.create(WebLogicJavaArchive.class, jarName + ".jar").addClasses(SampleInterface.class,
                SampleInterfaceImpl.class)
                .addAsManifestResource(new File(SharedJarTest.class.getClassLoader().getResource("sharedJarResources/jarResources/MANIFEST.MF").getPath()))
                .as(WebLogicJavaArchive.class);


        javaArchive.setSharedLibrary(true);


    }


    private void createEnterpriseArchive() {


        WebLogicWebArchive webArchive = ShrinkWrap.create(WebLogicWebArchive.class, warName + ".war").addPackage(SampleWS.class.getPackage())
                .addClasses(SampleWS.class)
                .addAsWebInfResource(new File(SharedJarTest.class.getClassLoader().getResource("sharedJarResources/warResources/weblogic.xml").getPath()))
                .addAsWebInfResource(new File(SharedJarTest.class.getClassLoader().getResource("sharedJarResources/warResources/web.xml").getPath()))
                .addAsWebResource(new File(SharedJarTest.class.getClassLoader().getResource("sharedJarResources/warResources/index.html").getPath()))
                .as(WebLogicWebArchive.class);


        enterpriseArchive = ShrinkWrap.create(WebLogicEnterpriseArchive.class, earName + ".ear").addAsModule(
                webArchive.as(WebArchive.class))
                .addAsManifestResource(new File(SharedJarTest.class.getClassLoader().getResource("sharedJarResources/earResources/application.xml").getPath()))
                .addAsManifestResource(new File(SharedJarTest.class.getClassLoader().getResource("sharedJarResources/earResources/MANIFEST.MF").getPath()))
                .as(WebLogicEnterpriseArchive.class);


    }



   // @Test
    public void deployFirstWar() throws DeploymentException {
        WebLogicWebArchive webArchive = ShrinkWrap.create(WebLogicWebArchive.class, "images.war")
//                 .addPackage(SampleWS.class.getPackage())
//                .addClasses(SampleWS.class)
//                .addAsWebInfResource(new File(org.jboss.shrinkwrap.weblogic.tests.ShrinkwrapWeblogicTestCase.class.getClassLoader().getResource("warResources/weblogic.xml").getPath()))
//                .addAsWebInfResource(new File(org.jboss.shrinkwrap.weblogic.tests.ShrinkwrapWeblogicTestCase.class.getClassLoader().getResource("warResources/web.xml").getPath()))
                .addAsManifestResource(new File(SharedJarTest.class.getClassLoader().getResource("sharedWarResources/sharedWarResources/MANIFEST.MF").getPath()))
                .addAsWebResource(new File(SharedJarTest.class.getClassLoader().getResource("sharedWarResources/sharedWarResources/index.html").getPath()))
                .as(WebLogicWebArchive.class);

        webArchive.setSharedLibrary(true);

        WeblogicUtils.doDeploy("images", webArchive);



    }

   // @Test
    public void deploySecondWar() throws DeploymentException {

        WebLogicWebArchive webArchive = ShrinkWrap.create(WebLogicWebArchive.class, "testwar.war").addPackage(SampleWS.class.getPackage())
                .addClasses(SampleWS.class)
                .addAsWebInfResource(new File(SharedJarTest.class.getClassLoader().getResource("sharedWarResources/warResources/weblogic.xml").getPath()))
                .as(WebLogicWebArchive.class);

        WeblogicUtils.doDeploy("testwar", webArchive);


    }


}
