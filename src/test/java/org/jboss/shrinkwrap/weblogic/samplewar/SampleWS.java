package org.jboss.shrinkwrap.weblogic.samplewar;


import org.jboss.shrinkwrap.weblogic.samplejar.SampleInterface;
import org.jboss.shrinkwrap.weblogic.samplejar.SampleInterfaceImpl;

import javax.ejb.Stateless;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;


/**
 * Sample webservice with a REST endpoint which is going to be packaged as a war file and deployed in the WebLogic
 * server
 * @author Jyotishman Nag
 */
@Path("/sampleWS")
@Stateless
public class SampleWS{

    /**
     * Static string which will be returned on a get request
     */
    public static final String GREETING = "Hello from Jar";

    /**
     * Instantiates an object of the {@link org.jboss.shrinkwrap.weblogic.samplejar.SampleInterfaceImpl} class and
     * uses it to return the {@link #GREETING} string on a GET request.
     * @return {@link #GREETING} string
     */
    @GET
    @Produces("application/json")
    public String doGet() {
        SampleInterface sampleInterface = new SampleInterfaceImpl();
        return sampleInterface.echo(GREETING);
    }
}
