package test.obiee.audit.webcat.core;

import obiee.audit.webcat.core.SharedObject;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SharedObjectTest {

	@Test
	public void TestNoAccess() throws Exception {
		Assert.assertEquals(SharedObject.getPrivilegeList(0, ""), "No Access");
	}

	@Test
	public void TestRead() throws Exception {
		Assert.assertEquals(SharedObject.getPrivilegeList(1, ""), "Read");
	}

	@Test
	public void TestDeleteRead() throws Exception {
		Assert.assertEquals(SharedObject.getPrivilegeList(9, ""), "Delete; Read");
	}

	@Test(expectedExceptions = Exception.class)
	public void TestCorruptWeighingValue() throws Exception {
		SharedObject.getPrivilegeList(-100, "");
	}

	@Test(expectedExceptions = Exception.class)
	public void TestInvalidWeighingValue() throws Exception {
		SharedObject.getPrivilegeList(99999, "");
	}
}
