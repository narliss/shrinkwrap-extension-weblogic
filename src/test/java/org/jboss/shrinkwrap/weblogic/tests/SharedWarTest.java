package org.jboss.shrinkwrap.weblogic.tests;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.weblogic.api.WebLogicWebArchive;
import org.jboss.shrinkwrap.weblogic.samplewar.SampleWS;
import org.jboss.shrinkwrap.weblogic.utils.WeblogicPropertiesConstants;
import org.jboss.shrinkwrap.weblogic.utils.WeblogicUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import weblogic.management.DeploymentException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Sample integration test that demonstrates creating a shared war using ShrinkWrap, deploying it in WebLogic as a
 * shared library, and then accessing the shared resource from another application deployed in WebLogic.
 *
 * @author Jyotishman Nag
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SharedWarTest extends SharedArchiveTest {
    /**
     * This is method is executed before all the tests are run.
     */
    @BeforeClass
    public static void setUp() {
        System.out.println("Running tests for: " + SharedWarTest.class.getCanonicalName());
    }

    /**
     * Undeploys the {@link #dependentWebArchive} and {@link #sharedWebArchive} using the {@link
     * org.jboss.shrinkwrap.weblogic.utils.WeblogicUtils#undeploy(String, org.jboss.shrinkwrap.api.Archive)} method.
     * <p/>
     * This method is executed after all the tests have been completed.
     */
    @AfterClass
    public static void tearDown() {
        try {
            WeblogicUtils.undeploy(dependentWarName, dependentWebArchive);
            WeblogicUtils.undeploy(sharedWarName, sharedWebArchive);
        } catch (Exception e) {
            fail("Unable to undeploy files " + e.getLocalizedMessage());
        }
    }

    /**
     * The {@link #sharedWebArchive} is created here and deployed using the {@link
     * org.jboss.shrinkwrap.weblogic.utils.WeblogicUtils#doDeploy(String, org.jboss.shrinkwrap.api.Archive)} method as
     * a shared library.
     * Note: We include an index.html file with the shared war.
     *
     * We use the {@link org.jboss.shrinkwrap.weblogic.utils.WeblogicUtils#findAllLibraryDeployments()}
     * util method to get a list of all the applications that have been deployed on the server, and assert that the
     * {@link #sharedWebArchive} is present in the list.
     */
    @Test
    public void deployFirstSharedWar() {
        sharedWebArchive = ShrinkWrap.create(WebLogicWebArchive.class, sharedWarName + ".war")
                .addPackage(SampleWS.class.getPackage())
                .addClasses(SampleWS.class)
                .addAsManifestResource(new File(SharedWarTest.class.getClassLoader()
                        .getResource("sharedWarTestResources/sharedWarResources/MANIFEST.MF").getPath()))
                .addAsWebResource(new File(SharedWarTest.class.getClassLoader()
                        .getResource("sharedWarTestResources/sharedWarResources/index.html").getPath()))
                .as(WebLogicWebArchive.class);

        sharedWebArchive.setSharedLibrary(true);

        try {
            WeblogicUtils.doDeploy(sharedWarName, sharedWebArchive);
        } catch (DeploymentException e) {
            fail("Unable to deploy shared war " + e.getMessage());
        }

        assertTrue(WeblogicUtils.findAllLibraryDeployments().contains(sharedWarName));
    }

    /**
     * The {@link #dependentWebArchive} is created here and deployed using the {@link
     * org.jboss.shrinkwrap.weblogic.utils.WeblogicUtils#doDeploy(String, org.jboss.shrinkwrap.api.Archive)} method as a
     * shared library.
     * <p/>
     * NOTE: We do not include an index.html file with the shared war, since it will be using the index.html that was
     * deployed with the {@link #sharedWebArchive}.
     *
     * We use the {@link org.jboss.shrinkwrap.weblogic.utils.WeblogicUtils#findAllAppDeployments()}
     * util method to get a list of all the applications that have been deployed on the server, and assert that the
     * {@link #dependentWarName} is present in the list.
     */
    @Test
    public void deploySecondDependentWar() {
        dependentWebArchive = ShrinkWrap.create(WebLogicWebArchive.class, dependentWarName + ".war")
                .addPackage(SampleWS.class.getPackage())
                .addClasses(SampleWS.class)
                .addAsWebInfResource(new File(SharedWarTest.class.getClassLoader()
                        .getResource("sharedWarTestResources/dependentWarResources/weblogic.xml").getPath()))
                .as(WebLogicWebArchive.class);

        try {
            WeblogicUtils.doDeploy(dependentWarName, dependentWebArchive);
        } catch (DeploymentException e) {
            fail("Unable to deploy dependent war " + e.getMessage());
        }

        assertTrue(WeblogicUtils.findAllAppDeployments().contains(dependentWarName));
    }

    /**
     * We load the contents from the context root of the {@link #dependentWebArchive} into a string. The {@link
     * #dependentWebArchive} does not contain an index.html, but it uses the index.html that was deployed inside of the
     * {@link #sharedWebArchive} library. We assert that this is true and the contents of the page is what we expect it
     * to be.
     */
    @Test
    public void testPageExists() {
        try {
            URL website = new URL("http://" + webLogicProperties.getProperty(WeblogicPropertiesConstants.HOSTNAME)
                    + ":" + webLogicProperties.getProperty(WeblogicPropertiesConstants.PORT_STRING)
                    + dependentWarContextRoot);

            URLConnection connection = website.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));

            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            assertTrue(response.toString().contains("This is a shared page"));
        } catch (MalformedURLException e) {
            fail("Could not connect to URL " + e.getMessage());
        } catch (IOException e) {
            fail("Could not connect to URL " + e.getMessage());
        }
    }
}
