package test.obiee.audit.webcat.objects.system;

import org.testng.annotations.Test;

import obiee.audit.webcat.objects.system.ApplicationRole;

public class ApplicationRoleTest {

	@Test(expectedExceptions = Exception.class)
	public void ApplicationRoleWithNoFile() throws Exception {
		new ApplicationRole(null);
	}
}
