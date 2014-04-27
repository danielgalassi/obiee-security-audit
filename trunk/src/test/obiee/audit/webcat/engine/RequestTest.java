package test.obiee.audit.webcat.engine;

import obiee.audit.webcat.engine.Request;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RequestTest {

	@DataProvider
	public String[][][] inconsistentArgs() {
		return new String[][][]{
				{{null}},
				{{"-a", "-b"}},
				{{"-webcat=c:\\data\temp"}},
				{{"-dashboards", "-privs"}}
				};
	}

	@Test(dataProvider = "inconsistentArgs", expectedExceptions = Exception.class)
	public void RequestThrowingExceptions(String[] args) throws Exception {
		new Request(args);
	}

	@Test
	public void RequestHelp() throws Exception {
		String[] args = {"-h"};
		new Request(args);
	}

	@Test
	public void RequestWithValidArguments() throws Exception {
		String[] args = {"-webcat=C:\\data\\workspace\\obiee-security-audit\\sampleCases\\SampleApp", "-dashboards", "-privs"};
		new Request(args);
	}
}
