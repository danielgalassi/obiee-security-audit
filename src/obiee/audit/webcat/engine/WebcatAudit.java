package obiee.audit.webcat.engine;

import java.io.InputStream;

import obiee.audit.webcat.utils.XMLUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WebcatAudit {

	private static final Logger logger = LogManager.getLogger(WebcatAudit.class.getName());

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
			logger.error("{} thrown while attempting to load {}", e.getClass().getCanonicalName(), rsc);
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
			logger.info("Initialising Webcat Parsing in progress...");
			webcat = new WebCatalog(webcatLocation);
			logger.info("Initialisation finished");
		}

		if (webcat != null && isPrivilegeAuditInvoked) {
			logger.info("Privilege audit in progress...");
			webcat.processWebCatPrivileges();
			logger.info("Privilege audit completed");
		}

		if (webcat != null && isDashboardAuditInvoked) {
			logger.info("Dashboard audit in progress...");
			webcat.processDashboards();
			logger.info("Dashboard Audit completed");
		}

		webcat.save();

		//Traverses the webcat if privilege or dashboard audits are requested 
		WebcatAudit audit = null;
		if (webcat != null && (isPrivilegeAuditInvoked || isDashboardAuditInvoked)) {
			audit = new WebcatAudit();
		}

		//Applying stylesheets to generate user friendly output in HTML
		if (webcat != null && isPrivilegeAuditInvoked) {
			logger.info("Creating Privilege Audit documentation...");
			stylesheet1 = audit.loadInternalResource("/obiee/audit/bundledApps/RolesMadeEasy.xsl");
			stylesheet2 = audit.loadInternalResource("/obiee/audit/bundledApps/FeaturesByRoleType.xsl");
			stylesheet3 = audit.loadInternalResource("/obiee/audit/bundledApps/FeaturesByRole.xsl");
			XMLUtils.applyStylesheet("Webcat.xml", stylesheet1, "RolesMadeEasy.xml");
			XMLUtils.applyStylesheet("RolesMadeEasy.xml", stylesheet2, "FeaturesByRoleType.html");
			XMLUtils.applyStylesheet("RolesMadeEasy.xml", stylesheet3, "FeaturesByRole.html");
			logger.info("Privilege Audit documentation completed");
		}

		//Applying stylesheets to generate user friendly output in HTML
		if (webcat != null && isDashboardAuditInvoked) {
			logger.info("Creating Dashboard Audit documentation...");
			stylesheet1 = audit.loadInternalResource("/obiee/audit/bundledApps/RolesMadeEasyForDashboards.xsl");
			stylesheet2 = audit.loadInternalResource("/obiee/audit/bundledApps/DashboardsByRoleType.xsl");
			XMLUtils.applyStylesheet("Webcat.xml", stylesheet1, "RolesMadeEasyForDashboards.xml");
			XMLUtils.applyStylesheet("RolesMadeEasyForDashboards.xml", stylesheet2, "DashboardsByRoleType.html");
			logger.info("Dashboard Audit documentation completed");
		}
	}
}
/*
 * Arguments: -w=path_to_Presentation_Catalogue_folder [-privs] [-dashboards]
 */
