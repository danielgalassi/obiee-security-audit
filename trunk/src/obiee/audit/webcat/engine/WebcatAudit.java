package obiee.audit.webcat.engine;

import java.io.InputStream;
import java.util.Vector;

import obiee.audit.webcat.core.WebCatalog;
import obiee.audit.webcat.utils.XMLUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WebcatAudit {

	private static final Logger logger = LogManager.getLogger(WebcatAudit.class.getName());

	private static boolean    isDashboardAuditInvoked = false;
	private static boolean    isPrivilegeAuditInvoked = false;
	private static Request                    request = null;
	private static Vector<InputStream>    stylesheets;

	public InputStream loadInternalResource(String rsc) {
		InputStream resource = null;
		try {
			resource = getClass().getResourceAsStream(rsc);
		} catch (Exception e) {
			logger.error("{} thrown while attempting to load {}", e.getClass().getCanonicalName(), rsc);
		}
		return resource;
	}

	private static void publishPrivilegeAuditResults() {
		logger.info("Creating Privilege Audit documentation...");
		XMLUtils.applyStylesheet("Webcat.xml", stylesheets.get(0), "RolesMadeEasy.xml");
		XMLUtils.applyStylesheet("RolesMadeEasy.xml", stylesheets.get(1), "FeaturesByRoleType.html");
		XMLUtils.applyStylesheet("RolesMadeEasy.xml", stylesheets.get(2), "FeaturesByRole.html");
		logger.info("Privilege Audit documentation completed");
	}

	private static void publishDashboardsAuditResults() {
		logger.info("Creating Dashboard Audit documentation...");
		XMLUtils.applyStylesheet("Webcat.xml", stylesheets.get(3), "RolesMadeEasyForDashboards.xml");
		XMLUtils.applyStylesheet("RolesMadeEasyForDashboards.xml", stylesheets.get(4), "DashboardsByRoleType.html");
		logger.info("Dashboard Audit documentation completed");
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

	private static void publishResults() {
		prepareAudit();
		if (isPrivilegeAuditInvoked) {
			publishPrivilegeAuditResults();
		}
		if (isDashboardAuditInvoked) {
			publishDashboardsAuditResults();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		WebCatalog webcat = null;

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
			isPrivilegeAuditInvoked = request.isPrivilegeAuditInvoked();
			isDashboardAuditInvoked = request.isDashboardAuditInvoked();
			webcat = new WebCatalog(request.getWebcatParam());
		} catch (Exception e) {
			logger.fatal("{} thrown while initialising Audit Engine. Exiting...", e.getClass().getCanonicalName());
			return;
		}
		logger.info("Initialisation finished");

		//Privileges need to be audited for both features (privs and dashboards)
		webcat.auditPrivileges();

		if (isDashboardAuditInvoked) {
			webcat.auditDashboards();
		}

		webcat.export();

		publishResults();
	}
}
/*
 * Arguments: -w=path_to_Presentation_Catalogue_folder [-privs -dashboards]
 */
