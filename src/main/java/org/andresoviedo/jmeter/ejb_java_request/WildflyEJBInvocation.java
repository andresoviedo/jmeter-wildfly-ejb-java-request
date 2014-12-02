package org.andresoviedo.jmeter.ejb_java_request;

import java.util.Properties;

import javax.naming.NamingException;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.jboss.ejb.client.ContextSelector;
import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.client.PropertiesBasedEJBClientConfiguration;
import org.jboss.ejb.client.remoting.ConfigBasedEJBClientContextSelector;

public class WildflyEJBInvocation extends AbstractJavaSamplerClient {

	// TODO: multithread can't use this
	static void initializeEJBClientContext(String remoteAppName, String remoteHostName, String remotePortNumber, String remoteUser,
			String remotePassword) {
		Properties properties = new Properties();
		properties.put("endpoint.name", remoteAppName);
		properties.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
		properties.put("remote.connections", "default");
		properties.put("remote.connection.default.host", remoteHostName);
		properties.put("remote.connection.default.port", remotePortNumber);
		properties.put("remote.connection.default.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "false");
		properties.put("remote.connection.default.username", remoteUser);
		properties.put("remote.connection.default.password", remotePassword);
		PropertiesBasedEJBClientConfiguration configuration = new PropertiesBasedEJBClientConfiguration(properties);
		final ContextSelector<EJBClientContext> ejbClientContextSelector = new ConfigBasedEJBClientContextSelector(configuration);
		EJBClientContext.setSelector(ejbClientContextSelector);
	}

	public SampleResult runTest(JavaSamplerContext context) {

		final String remoteHostName = context.getParameter("remote_hostname");
		final String remotePortNumber = context.getParameter("remote_port");
		final String remoteUser = context.getParameter("remote_user");
		final String remotePassword = context.getParameter("remote_password");
		final String remoteAppName = context.getParameter("remote_appName");

		final String remoteEjbModuleName = context.getParameter("remote_ejb_moduleName");
		final String remoteEjbInterface = context.getParameter("remote_ejb_interface");
		final String remoteEjbMethodName = context.getParameter("remote_ejb_method");
		final String remoteEjbName = context.getParameter("remote_ejb_name");

		SampleResult results = new SampleResult();
		results.sampleStart();

		try {

			Class<?> remoteEjbInterfaceClass = Class.forName(remoteEjbInterface);

			initializeEJBClientContext(remoteAppName, remoteHostName, remotePortNumber, remoteUser, remotePassword);

			Object remoteEJB = WildflyEJBServiceLocator.getEjb(remoteUser, remotePassword, "http-remoting://" + remoteHostName + ":"
					+ remotePortNumber, remoteEjbInterfaceClass, remoteAppName, remoteEjbModuleName, remoteEjbName);
			Object response = remoteEjbInterfaceClass.getMethod(remoteEjbMethodName, (Class<?>) null).invoke(remoteEJB,
					new Object[] { null, null, null, null, null, null, null });

			System.out.println(response);
			results.setSuccessful(true);
			results.setResponseCodeOK();
			results.setResponseMessage("EJB_RESPONSE '" + response + "'");
		} catch (NamingException e) {
			e.printStackTrace();
			results.setResponseMessage(e.getMessage());
			results.setSuccessful(false);
		} catch (Throwable e) {
			e.printStackTrace();
			results.setResponseMessage(e.getMessage());
			results.setSuccessful(false);
		}

		results.sampleEnd();
		return results;
	}

	@Override
	public Arguments getDefaultParameters() {
		Arguments args = new Arguments();

		args.addArgument("remote_hostname", "localhost");
		args.addArgument("remote_port", "8080");
		args.addArgument("remote_user", "testuser");
		args.addArgument("remote_password", "testpassword");
		args.addArgument("remote_appName", "myAppEAR");

		args.addArgument("remote_ejb_moduleName", "myEJB-0.1-SNAPSHOT");
		args.addArgument("remote_ejb_interface", "org.andresoviedo.fwk1.ejb.ArqEJBSrv.class");
		args.addArgument("remote_ejb_method", "call");
		args.addArgument("remote_ejb_name", "arqEJB");
		return args;
	}

}