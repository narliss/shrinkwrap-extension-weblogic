package org.jboss.shrinkwrap.weblogic.samplejar;

/**
 * Implementation of {@link org.jboss.shrinkwrap.weblogic.samplejar.SampleInterface}. The class will be packaged
 * as a shared Java archive and deployed in the WebLogic server.
 * @author Jyotishman Nag
 */
public class SampleInterfaceImpl implements SampleInterface {

    public String echo(String echoValue) {

        return echoValue;
    }
}
