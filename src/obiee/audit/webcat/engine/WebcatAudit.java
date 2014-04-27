package obiee.audit.webcat.engine;

import java.io.InputStream;
import java.util.Vector;

import obiee.audit.webcat.core.WebCatalog;
import obiee.audit.webcat.utils.XMLUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WebcatAudit {

	private static final Logger logger = LogManager.getLogger(WebcatAudit.class.getName());

	private static boolean isPrivilegeAuditInvoked = false;
	private static boolean isDashboardAuditInvoked = false;
	private static Vector<InputStream> stylesheets;
	private static Request request = null;

	public InputStream loadInternalResource(String rsc) {
		InputStream resource = null;
		try {
			resource = getClass().getResourceAsStream(rsc);
		} catch (Exception e) {
			logger.error("{} thrown while attempting to load {}", e.getClass().getCanonicalName(), rsc);
		}
		return resource;
	}

	private static void auditDashboards() {
		logger.info("Creating Dashboard Audit documentation...");
		XMLUtils.applyStylesheet("Webcat.xml", stylesheets.get(3), "RolesMadeEasyForDashboards.xml");
		XMLUtils.applyStylesheet("RolesMadeEasyForDashboards.xml", stylesheets.get(4), "DashboardsByRoleType.html");
		logger.info("Dashboard Audit documentation completed");
	}

	private static void auditPrivileges() {
		logger.info("Creating Privilege Audit documentation...");
		XMLUtils.applyStylesheet("Webcat.xml", stylesheets.get(0), "RolesMadeEasy.xml");
		XMLUtils.applyStylesheet("RolesMadeEasy.xml", stylesheets.get(1), "FeaturesByRoleType.html");
		XMLUtils.applyStylesheet("RolesMadeEasy.xml", stylesheets.get(2), "FeaturesByRole.html");
		logger.info("Privilege Audit documentation completed");
	}

	private static void prepareAudit() {
		WebcatAudit audit = new WebcatAudit();
		stylesheets = new Vector<InputStream>();
		stylesheets.add(audit.loadInternalResource("/obiee/audit/bundledApps/RolesMadeEasy.xsl"));
		stylesheets.add(audit.loadInternalResource("/obiee/audit/bundledApps/FeaturesByRoleType.xsl"));
		stylesheets.add(audit.loadInternalResource("/obiee/audit/bundledApps/FeaturesByRole.xsl"));
		stylesheets.add(audit.loadInternalResource("/obiee/audit/bundledApps/RolesMadeEasyForDashboards.xsl"));
		stylesheets.add(audit.loadInternalResource("/obiee/audit/bundledApps/DashboardsByRoleType.xsl"));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		WebCatalog webcat = null;
//		String webcatLocation = null;

		try {
			request = new Request(args);
		} catch (Exception e) {
			logger.fatal("{} thrown while evaluating command line arguments", e.getClass().getCanonicalName());
			return;
		}

		if (request.isHelpWanted()) {
			return;
		}

		try {
			logger.info("Initialising Webcat Parsing in progress...");
//			webcatLocation = request.getWebcatParam();
			isPrivilegeAuditInvoked = request.isPrivilegeAuditInvoked();
			isDashboardAuditInvoked = request.isDashboardAuditInvoked();
//			webcat = new WebCatalog(webcatLocation);
			webcat = new WebCatalog(request.getWebcatParam());
		} catch (Exception e) {
			logger.fatal("{} thrown while initialising Audit Engine. Exiting...", e.getClass().getCanonicalName());
			return;
		}
		logger.info("Initialisation finished");

		if (isPrivilegeAuditInvoked) {
			logger.info("Privilege audit in progress...");
			webcat.processWebCatPrivileges();
			logger.info("Privilege audit completed");
		}

		if (isDashboardAuditInvoked) {
			logger.info("Dashboard audit in progress...");
			webcat.processDashboards();
			logger.info("Dashboard Audit completed");
		}

		webcat.save();

		//Traverses the webcat if privilege or dashboard audits are requested 
		if (isPrivilegeAuditInvoked || isDashboardAuditInvoked) {
			prepareAudit();
		}

		if (isPrivilegeAuditInvoked) {
			auditPrivileges();
		}

		if (isDashboardAuditInvoked) {
			auditDashboards();
		}
	}
}
/*
 * Arguments: -w=path_to_Presentation_Catalogue_folder [-privs] [-dashboards]
 */
