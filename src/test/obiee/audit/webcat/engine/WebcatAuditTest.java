package test.obiee.audit.webcat.engine;

import obiee.audit.webcat.engine.WebcatAudit;

import org.testng.annotations.Test;

public class WebcatAuditTest {

	@Test
	public void main() {
		String s[] = {"-w=.\\sampleCases\\SampleApp","-privs","-dashboards"};
		WebcatAudit audit = new WebcatAudit();
		audit.main(s);
	}
}
