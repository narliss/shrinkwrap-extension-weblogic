package org.jboss.shrinkwrap.weblogic.samplejar;

/**
 * A sample interface which is being packaged as a shared java archive to be deployed in the WebLogic server.
 * @author Jyotishman Nag
 */
public interface SampleInterface {

    public String echo(String echoValue);
}
