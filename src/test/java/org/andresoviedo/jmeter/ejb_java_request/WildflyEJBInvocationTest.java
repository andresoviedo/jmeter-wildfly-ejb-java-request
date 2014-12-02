package org.andresoviedo.jmeter.ejb_java_request;

import org.andresoviedo.fwk1.ejb.AppService;
import org.junit.Assert;
import org.junit.Test;

public class WildflyEJBInvocationTest {

	@Test
	public void test() throws Exception {

		// UserTransaction tx = null;
		Object response = null;
		try {

			// tx = (UserTransaction) context.lookup("UserTransaction");

			try {
				// tx.begin();

				AppService remoteEjb = WildflyEJBServiceLocator.getEjb("testuser", "testpassword", "http-remoting://localhost:8080",
						AppService.class, "myAppEAR", "myEJB-0.1-SNAPSHOT", "myBean");

				remoteEjb.invoke(null);

				// tx.commit();

			} catch (Exception ex) {
				ex.printStackTrace();
				// if (tx != null) {
				// // try {
				// // tx.rollback();
				// // } catch (Exception e) {
				// // e.printStackTrace();
				// // }
				// }
			}

			System.out.println(response);

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		} catch (Throwable e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

}
