package test.obiee.audit.webcat.engine;

import org.testng.annotations.Test;

import obiee.audit.webcat.engine.WebcatAudit;

public class WebcatAuditTest {

	@Test
	public static void main() {
		String s[] = {"-w=.\\sampleCases\\SampleApp","-privs","-dashboards"};
		WebcatAudit audit = new WebcatAudit();
		audit.main(s);
	}
}
