package org.jboss.shrinkwrap.weblogic.samplewar;

import javax.ws.rs.core.Application;

import javax.ws.rs.ApplicationPath;

/**
 * Used to define an application path for a sample webservice which will be packaged as a war file and deployed inside
 * of WebLogic
 * @author Jyotishman Nag
 */
@ApplicationPath("/rest")
public class JaxRsActivator extends Application{
}
