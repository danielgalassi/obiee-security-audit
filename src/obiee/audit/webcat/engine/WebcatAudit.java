package obiee.audit.webcat.engine;

import java.io.InputStream;

import obiee.audit.webcat.utils.XMLUtils;

public class WebcatAudit {

	private static boolean isPrivilegeAuditInvoked = false;
	private static boolean isDashboardAuditInvoked = false;
	private static InputStream stylesheet1;
	private static InputStream stylesheet2;
	private static InputStream stylesheet3;

	private InputStream loadInternalResource(String rsc) {
		InputStream resource = null;
		try {
			resource = getClass().getResourceAsStream(rsc);
		} catch (Exception e) {
			System.out.println("Exception loading: " + rsc);
			e.printStackTrace();
		}
		return resource;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		WebCatalog webcat = null;
		String webcatLocation = null;

		//picking up parameters
		for (String argument : args)
		{
			//Web Catalog Location
			if (argument.startsWith("-w="))
				webcatLocation = argument.replaceAll("-w=", "");
			if (argument.startsWith("-privs"))
				isPrivilegeAuditInvoked = true;
			if (argument.startsWith("-dashboards"))
				isDashboardAuditInvoked = true;
		}

		if (webcatLocation != null) {
			System.out.println("Initialising Webcat Parsing in progress...");
			webcat = new WebCatalog(webcatLocation);
			System.out.println("Initialisation finished.");
		}

		if (webcat != null && isPrivilegeAuditInvoked) {
			System.out.println("Privilege audit in progress...");
			webcat.processWebCatPrivileges();
			System.out.println("Privilege audit completed.");
		}

		if (webcat != null && isDashboardAuditInvoked) {
			System.out.println("Dashboard audit in progress...");
			webcat.processDashboards();
			System.out.println("Dashboard Audit completed.");
		}

		webcat.save();

		//Traverses the webcat if privilege or dashboard audits are requested 
		WebcatAudit audit = null;
		if (webcat != null && (isPrivilegeAuditInvoked || isDashboardAuditInvoked)) {
			audit = new WebcatAudit();
		}

		//Applying stylesheets to generate user friendly output in HTML
		if (webcat != null && isPrivilegeAuditInvoked) {
			System.out.println("Creating Privilege Audit documentation...");
			stylesheet1 = audit.loadInternalResource("/obiee/audit/bundledApps/RolesMadeEasy.xsl");
			stylesheet2 = audit.loadInternalResource("/obiee/audit/bundledApps/FeaturesByRoleType.xsl");
			stylesheet3 = audit.loadInternalResource("/obiee/audit/bundledApps/FeaturesByRole.xsl");
			XMLUtils.xsl4Files("Webcat.xml", stylesheet1, "RolesMadeEasy.xml");
			XMLUtils.xsl4Files("RolesMadeEasy.xml", stylesheet2, "FeaturesByRoleType.html");
			XMLUtils.xsl4Files("RolesMadeEasy.xml", stylesheet3, "FeaturesByRole.html");
			System.out.println("Privilege Audit documentation completed.");
		}

		//Applying stylesheets to generate user friendly output in HTML
		if (webcat != null && isDashboardAuditInvoked) {
			System.out.println("Creating Dashboard Audit documentation...");
			stylesheet1 = audit.loadInternalResource("/obiee/audit/bundledApps/RolesMadeEasyForDashboards.xsl");
			stylesheet2 = audit.loadInternalResource("/obiee/audit/bundledApps/DashboardsByRoleType.xsl");
			XMLUtils.xsl4Files("Webcat.xml", stylesheet1, "RolesMadeEasyForDashboards.xml");
			XMLUtils.xsl4Files("RolesMadeEasyForDashboards.xml", stylesheet2, "DashboardsByRoleType.html");
			System.out.println("Dashboard Audit documentation completed.");
		}
	}
}
/*
 * Arguments: -w=path_to_Presentation_Catalogue_folder [-privs] [-dashboards]
 */
