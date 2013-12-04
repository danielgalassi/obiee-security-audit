package webcatAudit;

import java.io.InputStream;

import utils.XMLUtils;


public class WebcatAudit {

	private static boolean isPrivilegeAuditInvoked = false;
	private static boolean isDashboardAuditInvoked = false;
	private static InputStream insXSL1;
	private static InputStream insXSL2;
	private static InputStream insXSL3;

	private InputStream istrInternalResource(String rsc) {
		InputStream isRsc = null;
		try {
			isRsc = getClass().getResourceAsStream(rsc);
		} catch (Exception e) {
			System.out.println("Exception loading: " + rsc);
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

		if (wc != null && isPrivilegeAuditInvoked) {
			System.out.println("Privilege audit in progress...");
			wc.processWebCatPrivileges();
			System.out.println("Privilege audit completed.");
		}

		if (wc != null && isDashboardAuditInvoked) {
			System.out.println("Dashboard audit in progress...");
			wc.processDashboards();
			System.out.println("Dashboard Audit completed.");
		}

		wc.save();

		//Traverses the webcat if privilege or dashboard audits are requested 
		WebcatAudit w = null;
		if (wc != null && (isPrivilegeAuditInvoked || isDashboardAuditInvoked)) {
			w = new WebcatAudit();
		}

		//Applying stylesheets to generate user friendly output in HTML 
		if (wc != null && isPrivilegeAuditInvoked) {
			//WebcatAudit w = new WebcatAudit();
			insXSL1 = w.istrInternalResource("/bundledApps/RolesMadeEasy.xsl");
			insXSL2 = w.istrInternalResource("/bundledApps/FeaturesByRoleType.xsl");
			insXSL3 = w.istrInternalResource("/bundledApps/FeaturesByRole.xsl");
			XMLUtils.xsl4Files("Webcat.xml", insXSL1, "RolesMadeEasy.xml");
			XMLUtils.xsl4Files("RolesMadeEasy.xml", insXSL2, "FeaturesByRoleType.html");
			XMLUtils.xsl4Files("RolesMadeEasy.xml", insXSL3, "FeaturesByRole.html");
		}

		//Applying stylesheets to generate user friendly output in HTML 
		if (wc != null && isDashboardAuditInvoked) {
			//WebcatAudit w = new WebcatAudit();
			insXSL1 = w.istrInternalResource("/bundledApps/RolesMadeEasyForDashboards.xsl");
			insXSL2 = w.istrInternalResource("/bundledApps/DashboardsByRoleType.xsl");
			XMLUtils.xsl4Files("Webcat.xml", insXSL1, "RolesMadeEasyForDashboards.xml");
			XMLUtils.xsl4Files("RolesMadeEasyForDashboards.xml", insXSL2, "DashboardsByRoleType.html");
		}
	}
}
/*
 * Arguments: -w=path_to_Presentation_Catalogue_folder [-privs] [-dashboards]
 */
