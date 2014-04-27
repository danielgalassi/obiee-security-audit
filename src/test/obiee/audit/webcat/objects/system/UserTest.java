package test.obiee.audit.webcat.objects.system;

import obiee.audit.webcat.objects.system.User;

import org.testng.annotations.Test;

public class UserTest {

  @Test(expectedExceptions = Exception.class)
  public void User() throws Exception {
    User user = new User(null);
  }
}
