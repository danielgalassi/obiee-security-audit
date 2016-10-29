package test.obiee.audit.webcat.objects.system;

import org.testng.annotations.Test;

import obiee.audit.webcat.objects.system.User;

public class UserTest {

  @Test(expectedExceptions = Exception.class)
  public void User() throws Exception {
    new User(null);
  }
}
