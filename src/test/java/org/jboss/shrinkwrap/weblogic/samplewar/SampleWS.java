package org.jboss.shrinkwrap.weblogic.samplewar;


import org.jboss.shrinkwrap.weblogic.samplejar.SampleInterface;
import org.jboss.shrinkwrap.weblogic.samplejar.SampleInterfaceImpl;

import javax.ejb.Stateless;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;


/**
 * Created by jnag on 6/10/14.
 */
@Path("/sampleWS")
@Stateless
public class SampleWS{

    public static final String GREETING = "Hello from Jar";

    @GET
    @Produces("application/json")
    public String doGet() throws NamingException {

        SampleInterface sampleInterface = new SampleInterfaceImpl();
        return sampleInterface.echo(GREETING);




    }
}
