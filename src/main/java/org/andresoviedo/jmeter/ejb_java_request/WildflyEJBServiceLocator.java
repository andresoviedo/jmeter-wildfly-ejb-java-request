package org.andresoviedo.jmeter.ejb_java_request;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public final class WildflyEJBServiceLocator {

	public static <T> T getEjb(String userName, String password, String providerUrl, Class<T> clazz, String appName, String moduleName,
			String beanName) throws NamingException {
		return getEjbImpl(providerUrl, userName, password, clazz, appName, moduleName, beanName);

	}

	@SuppressWarnings("unchecked")
	private static <T> T getEjbImpl(String providerUrl, String userName, String password, Class<T> clazz, String appName,
			String moduleName, String beanName) throws NamingException {
		if (clazz == null) {
			throw new IllegalArgumentException("Remote interface can't be null");
		}
		if (appName == null) {
			throw new IllegalArgumentException("Remote application name can't be null");
		}
		if (beanName == null) {
			throw new IllegalArgumentException("Remote ejb name can't be null");
		}
		if (moduleName == null) {
			throw new IllegalArgumentException("Remote ejb module name can't be null");
		}

		T ejb = null;
		try {
			String name = appName + "/" + moduleName + "/" + beanName + "!" + clazz.getName();

			InitialContext initialContext = buildInitialContext(providerUrl, userName, password);

			ejb = (T) initialContext.lookup(name);

		} catch (NamingException ex) {
			String errorMessage = String.format("GenericEJBProxy: Error al obtener el ejb %s (%s) %s", clazz.getSimpleName(),
					clazz.getName(), ex.getMessage());
			System.out.println(errorMessage);
			throw ex;
		}
		return ejb;
	}

	private static InitialContext buildInitialContext(String providerUrl, String userName, String password) throws NamingException {
		Hashtable<Object, Object> configuration = new Hashtable<Object, Object>();

		configuration.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
		configuration.put(Context.PROVIDER_URL, providerUrl);
		configuration.put(Context.SECURITY_PRINCIPAL, userName);
		configuration.put(Context.SECURITY_CREDENTIALS, password);

		InitialContext initialContext = new InitialContext(configuration);
		return initialContext;
	}

}