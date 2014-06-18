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
import org.jboss.shrinkwrap.weblogic.samplejar.SampleInterface;
import org.jboss.shrinkwrap.weblogic.samplejar.SampleInterfaceImpl;
import org.jboss.shrinkwrap.weblogic.samplewar.SampleWS;
import org.jboss.shrinkwrap.weblogic.utils.WeblogicPropertiesConstants;
import org.jboss.shrinkwrap.weblogic.utils.WeblogicUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import weblogic.management.DeploymentException;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Sample integration test that demonstrates creating a shared jar using ShrinkWrap, deploying it in WebLogic as a
 * shared library, and using it from a web application (created using ShrinkWrap too), deployed in WebLogic.
 *
 * @author Jyotishman Nag
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SharedJarTest extends SharedArchiveTest {
    /**
     * This is method is executed before all the tests are run.
     */
    @BeforeClass
    public static void setUp() {
        System.out.println("Running tests for: " + SharedJarTest.class.getCanonicalName());
    }

    /**
     * Undeploys the {@link #sharedJavaArchive} and {@link #dependentEnterpriseArchive} using the {@link
     * org.jboss.shrinkwrap.weblogic.utils.WeblogicUtils#undeploy(String, org.jboss.shrinkwrap.api.Archive)} method.
     * <p/>
     * This method is executed after all the tests have been completed.
     */
    @AfterClass
    public static void tearDown() {
        try {
            WeblogicUtils.undeploy(dependentEarName, dependentEnterpriseArchive);
            WeblogicUtils.undeploy(sharedJarName, sharedJavaArchive);
        } catch (Exception e) {
            fail("Unable to undeploy files " + e.getLocalizedMessage());
        }
    }

    /**
     * This test is executed first. We annotated the class with the test order, so that tests are run in ascending
     * alphabetical order based on their method signatures.
     * <p/>
     * We use ShrinkWrap to create the shared {@link #sharedJavaArchive} jar, containing {@link
     * org.jboss.shrinkwrap.weblogic.samplejar.SampleInterface} and {@link org.jboss.shrinkwrap.weblogic.samplejar.SampleInterfaceImpl}
     * classes and deploy it to the WebLogic server using the {@link org.jboss.shrinkwrap.weblogic.utils.WeblogicUtils#doDeploy(String,
     * org.jboss.shrinkwrap.api.Archive)} utils method.
     *
     * We use the {@link org.jboss.shrinkwrap.weblogic.utils.WeblogicUtils#findAllLibraryDeployments()}
     * util method to get a list of all the applications that have been deployed on the server, and assert that the jar
     * we just deployed is present in the list.
     */
    @Test
    public void deployFirstSharedJar() {
        sharedJavaArchive =
                ShrinkWrap.create(WebLogicJavaArchive.class, sharedJarName + ".jar").addClasses(SampleInterface.class,
                        SampleInterfaceImpl.class)
                        .addAsManifestResource(new File(SharedJarTest.class.getClassLoader()
                                .getResource("sharedJarTestResources/jarResources/MANIFEST.MF").getPath()))
                        .as(WebLogicJavaArchive.class);

        sharedJavaArchive.setSharedLibrary(true);

        try {
            WeblogicUtils.doDeploy(sharedJarName, sharedJavaArchive);
        } catch (DeploymentException e) {
            fail("Unable to deploy shared jar " + e.getMessage());
        }

        assertTrue(WeblogicUtils.findAllLibraryDeployments().contains(sharedJarName));
    }

    /**
     * This test is executed following the completion of the {@link #deployFirstSharedJar()} method.
     * <p/>
     * We use ShrinkWrap to create a war containing {@link org.jboss.shrinkwrap.weblogic.samplewar.SampleWS} class,
     * following which we package the war we just created as an ear and deploy it to the WebLogic server using the
     * {@link org.jboss.shrinkwrap.weblogic.utils.WeblogicUtils#doDeploy(String, org.jboss.shrinkwrap.api.Archive)}
     * utils method.
     * <p/>
     * We use the {@link org.jboss.shrinkwrap.weblogic.utils.WeblogicUtils#findAllAppDeployments()} util method to get a
     * list of all the applications that have been deployed on the server, and assert that the ear we just deployed is
     * present in the list.
     */
    @Test
    public void deploySecondDependentEar() {
        dependentWebArchive = ShrinkWrap.create(WebLogicWebArchive.class, dependentWarName + ".war")
                .addPackage(SampleWS.class.getPackage())
                .addClasses(SampleWS.class)
                .addAsWebInfResource(new File(SharedJarTest.class.getClassLoader()
                        .getResource("sharedJarTestResources/warResources/weblogic.xml").getPath()))
                .addAsWebInfResource(new File(
                        SharedJarTest.class.getClassLoader().getResource("sharedJarTestResources/warResources/web.xml")
                                .getPath()))
                .addAsWebResource(new File(SharedJarTest.class.getClassLoader()
                        .getResource("sharedJarTestResources/warResources/index.html").getPath()))
                .as(WebLogicWebArchive.class);

        dependentEnterpriseArchive =
                ShrinkWrap.create(WebLogicEnterpriseArchive.class, dependentEarName + ".ear").addAsModule(
                        dependentWebArchive.as(WebArchive.class))
                        .addAsManifestResource(new File(SharedJarTest.class.getClassLoader()
                                .getResource("sharedJarTestResources/earResources/application.xml").getPath()))
                        .as(WebLogicEnterpriseArchive.class);

        try {
            WeblogicUtils.doDeploy(dependentEarName, dependentEnterpriseArchive);
        } catch (DeploymentException e) {
            fail("Unable to deploy shared jar " + e.getMessage());
        }

        assertTrue(WeblogicUtils.findAllAppDeployments().contains(dependentEarName));
    }

    /**
     * The war packaged in the ear that was deployed using the {@link #deploySecondDependentEar()} method contains a
     * REST endpoint. The endpoint instantiates an object of a class present in the shared jar, and uses the class to return
     * a string on a GET request.
     * <p/>
     * We perform a GET request on the endpoint and assert that the value returned matches the string from the war file.
     * This validates that the shared jar was created and deployed correctly since we just used it from another
     * application.
     */
    @Test
    public void testWebService() {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource service = client.resource("http://" + webLogicProperties.getProperty(WeblogicPropertiesConstants.HOSTNAME)
                + ":" + webLogicProperties.getProperty(WeblogicPropertiesConstants.PORT_STRING) + dependentWarContextRoot +
                "/rest/sampleWS");

        assertEquals(SampleWS.GREETING, service.get(String.class));
    }
}
