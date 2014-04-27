package test.obiee.audit.webcat.objects.system;

import obiee.audit.webcat.objects.system.ApplicationRole;

import org.testng.annotations.Test;

public class ApplicationRoleTest {

	@Test(expectedExceptions = Exception.class)
	public void ApplicationRoleWithNoFile() throws Exception {
		ApplicationRole role = new ApplicationRole(null);
	}
}
