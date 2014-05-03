package test.obiee.audit.webcat.core;

import obiee.audit.webcat.core.StandardSecuritySettings;

import org.testng.Assert;
import org.testng.annotations.Test;

public class StandardSecuritySettingsTest {

	StandardSecuritySettings s = new StandardSecuritySettings();

	@Test
	public void getIndexFor65535() {
		Assert.assertEquals(s.getIndex(65535), 0);
	}

	@Test
	public void getIndexFor16() {
		Assert.assertEquals(s.getIndex(16), 5);
	}

	@Test
	public void getIndexFor365() {
		Assert.assertEquals(s.getIndex(365), -1);
	}

	@Test
	public void getIndexForFullControl() {
		Assert.assertEquals(s.getIndexForWeighing("Full Control"), 0);
	}

	@Test
	public void getIndexForChangePermissions() {
		Assert.assertEquals(s.getIndexForWeighing("Change Permissions"), 5);
	}

	@Test
	public void getIndexForInvalidPermission() {
		Assert.assertEquals(s.getIndexForWeighing("Not A Valid Permission"), -1);
	}

	@Test (expectedExceptions = Exception.class)
	public void matchClosestWeighingWithInvalidValue() throws Exception {
		s.matchClosestWeighingForValue(-10);
	}

	@Test
	public void matchClosestWeighingWith17() throws Exception {
		Assert.assertEquals(s.matchClosestWeighingForValue(17), 16);
	}

	@Test
	public void matchClosestWeighingWith2048() throws Exception {
		Assert.assertEquals(s.matchClosestWeighingForValue(2048), 2048);
	}

	@Test
	public void getPermissionForValue() {
		Assert.assertEquals(s.getPermissionForValue(1), "Read");
	}

	@Test
	public void getPermissionForInvalidValue() {
		Assert.assertEquals(s.getPermissionForValue(-1), null);
	}

	@Test
	public void getPermissionForValue2() {
		Assert.assertEquals(s.getPermissionForValue(2), "Traverse");
	}

	@Test
	public void size() {
		Assert.assertEquals(s.size(), 13);
	}
}
