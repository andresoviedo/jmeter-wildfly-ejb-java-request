package org.andresoviedo.fwk1.ejb;

@javax.ejb.Remote
public interface AppService {

	Object invoke(Object to) throws Exception;
}
