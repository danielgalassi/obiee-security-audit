package webcatAudit;

import java.io.InputStream;


public class WebcatAudit {

	private static boolean isPrivilegeAuditInvoked = false;
	private static boolean isDashboardAuditInvoked = false;
	private static InputStream insXSL1;
	private static InputStream insXSL2;
	private static InputStream insXSL3;

	private InputStream istrInternalResource(String rsc) {
		InputStream isRsc = null;
		try {
			isRsc = getClass().getClassLoader().getResourceAsStream(rsc);
		} catch (Exception e) {
			System.out.println("istrInternalResource: " + rsc);
			e.printStackTrace();
		}
		return isRsc;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		WebCatalog wc = null;
		String sWebcatLocation = null;

		//picking up parameters
		for (String cmdLineArg : args)
        {
			//Web Catalog Location
			if (cmdLineArg.startsWith("-w="))
				sWebcatLocation = cmdLineArg.replaceAll("-w=", "");
			if (cmdLineArg.startsWith("-privs"))
				isPrivilegeAuditInvoked = true;
			if (cmdLineArg.startsWith("-dashboards"))
				isDashboardAuditInvoked = true;
        }

		if (sWebcatLocation != null)
			wc = new WebCatalog(sWebcatLocation);

		System.out.println("Privilege audit in progress...");
		if (wc != null && isPrivilegeAuditInvoked)
			wc.processWebCatPrivileges();
		System.out.println("Privilege audit completed.");

		System.out.println("Dashboard audit in progress...");
		if (wc != null && isDashboardAuditInvoked)
			wc.processDashboards();
		System.out.println("Dashboard Audit completed.");

		System.out.println(wc);
		System.out.println(isPrivilegeAuditInvoked);
		System.out.println(isDashboardAuditInvoked);

		if (wc != null && isPrivilegeAuditInvoked) {
			WebcatAudit w = new WebcatAudit();
			insXSL1 = w.istrInternalResource("bundledApps/RolesMadeEasy.xsl");
			insXSL2 = w.istrInternalResource("bundledApps/FeaturesByRoleType.xsl");
			insXSL3 = w.istrInternalResource("bundledApps/FeaturesByRoleType.xsl");
		}

		if (wc != null && isDashboardAuditInvoked) {
			WebcatAudit w = new WebcatAudit();
			insXSL1 = w.istrInternalResource("bundledApps/RolesMadeEasyForDashboards.xsl");
			insXSL2 = w.istrInternalResource("bundledApps/DashboardsByRoleType.xsl");
		}

		wc.save();
	}
}
