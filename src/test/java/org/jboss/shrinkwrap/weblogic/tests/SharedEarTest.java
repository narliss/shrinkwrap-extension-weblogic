package org.jboss.shrinkwrap.weblogic.tests;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
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
 * Sample integration test that demonstrates creating a shared ear using ShrinkWrap, deploying it in WebLogic as a
 * shared library, and then accessing the shared resource from another enterprise application deployed in the same
 * WebLogic server.
 *
 * @author Jyotishman Nag
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SharedEarTest extends SharedArchiveTest {

    /**
     * This method is executed before all the tests are run.
     */
    @BeforeClass
    public static void setUp() {
        System.out.println("Running tests for: " + SharedEarTest.class.getCanonicalName());
    }

    /**
     * Undeploys the {@link #dependentEnterpriseArchive} and {@link #sharedEnterpriseArchive} archives, using the {@link
     * org.jboss.shrinkwrap.weblogic.utils.WeblogicUtils#undeploy(String, org.jboss.shrinkwrap.api.Archive)} method.
     */
    @AfterClass
    public static void tearDown() {
        try {
            WeblogicUtils.undeploy(dependentEarName, dependentEnterpriseArchive);
            WeblogicUtils.undeploy(sharedEarName, sharedEnterpriseArchive);
        } catch (Exception e) {
            fail("Unable to undeploy files " + e.getLocalizedMessage());
        }
    }

    /**
     * Creates an enterprise archive containing a library jar and a web application. The {@link
     * #sharedEnterpriseArchive} is deployed as a shared library on the WebLogic server.
     * <p/>
     * We use the {@link org.jboss.shrinkwrap.weblogic.utils.WeblogicUtils#findAllLibraryDeployments()} util method to
     * get a list of all the libraries that have been deployed on the server, and assert that the {@link
     * #sharedEnterpriseArchive} is present in the list. This validates that the archive was deployed correctly.
     */
    @Test
    public void deployFirstSharedEar() {
        WebLogicJavaArchive javaArchive = ShrinkWrap.create(WebLogicJavaArchive.class, sharedJarName + ".jar")
                .addClasses(SampleInterfaceImpl.class, SampleInterface.class)
                .as(WebLogicJavaArchive.class);

        WebLogicWebArchive webArchive = ShrinkWrap.create(WebLogicWebArchive.class, sharedWarName + ".war")
                .addPackage(SampleWS.class.getPackage())
                .addClasses(SampleWS.class)
                .addAsWebResource(new File(SharedJarTest.class.getClassLoader()
                        .getResource("sharedJarTestResources/warResources/index.html").getPath()))
                .as(WebLogicWebArchive.class);

        sharedEnterpriseArchive = ShrinkWrap.create(WebLogicEnterpriseArchive.class, sharedEarName + ".ear")
                .addAsManifestResource(new File(SharedEarTest.class.getClassLoader()
                        .getResource("sharedEarTestResources/sharedEarResources/application.xml").getPath()))
                .addAsManifestResource(new File(SharedEarTest.class.getClassLoader()
                        .getResource("sharedEarTestResources/sharedEarResources/MANIFEST.MF").getPath()))
                .addAsLibrary(javaArchive.as(JavaArchive.class))
                .addAsModule(webArchive.as(WebArchive.class))
                .as(WebLogicEnterpriseArchive.class);

        sharedEnterpriseArchive.setSharedLibrary(true);
        try {
            WeblogicUtils.doDeploy(sharedEarName, sharedEnterpriseArchive);
        } catch (DeploymentException e) {
            fail("Unable to deploy shared war " + e.getMessage());
        }

        assertTrue(WeblogicUtils.findAllLibraryDeployments().contains(sharedEarName));
    }

    /**
     * Creates the  {@link #dependentEnterpriseArchive} enterprise archive containing a web application and deploys it
     * on the weblogic server.
     * <p/>
     * We use the {@link org.jboss.shrinkwrap.weblogic.utils.WeblogicUtils#findAllAppDeployments()} util method to get a
     * list of all the applications that have been deployed on the server, and assert that the {@link
     * #dependentEarName}is present in the list.
     */
    @Test
    public void deploySecondDependentEar() {
        WebLogicWebArchive dependentWebArchive = ShrinkWrap.create(WebLogicWebArchive.class, dependentWarName + ".war")
                .addPackage(SampleWS.class.getPackage())
                .addClasses(SampleWS.class)
                .addAsWebResource(new File(SharedJarTest.class.getClassLoader()
                        .getResource("sharedJarTestResources/warResources/index.html").getPath()))
                .as(WebLogicWebArchive.class);

        dependentEnterpriseArchive = ShrinkWrap.create(WebLogicEnterpriseArchive.class, dependentEarName + ".ear")
                .addAsManifestResource(new File(SharedEarTest.class.getClassLoader()
                        .getResource("sharedEarTestResources/dependentEarResources/application.xml").getPath()))
                .addAsManifestResource(new File(SharedEarTest.class.getClassLoader()
                        .getResource("sharedEarTestResources/dependentEarResources/weblogic-application.xml")
                        .getPath()))
                .addAsModule(dependentWebArchive.as(WebArchive.class))
                .as(WebLogicEnterpriseArchive.class);
        try {
            WeblogicUtils.doDeploy(dependentEarName, dependentEnterpriseArchive);
        } catch (DeploymentException e) {
            fail("Unable to deploy shared war " + e.getMessage());
        }

        assertTrue(WeblogicUtils.findAllAppDeployments().contains(dependentEarName));
    }

    /**
     * The {@link #dependentEnterpriseArchive} contains a web application with a REST point which uses a library that
     * was packaged in the {@link #sharedEnterpriseArchive}. The endpoint instantiates an object of a class present in
     * the shared library, and uses the class to return a string on a GET request.
     * <p/>
     * We perform a GET request on the endpoint and assert that the value returned matches the string from the war file.
     * This validates that the shared jar was created and deployed correctly since we just accessed it from another
     * application.
     */
    @Test
    public void testWebService() {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);

        WebResource service =
                client.resource("http://" + webLogicProperties.getProperty(WeblogicPropertiesConstants.HOSTNAME)
                        + ":" + webLogicProperties.getProperty(WeblogicPropertiesConstants.PORT_STRING) +
                        dependentWarContextRoot +
                        "/rest/sampleWS");

        assertEquals(SampleWS.GREETING, service.get(String.class));
    }
}
