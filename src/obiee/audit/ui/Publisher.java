/**
 * 
 */
package obiee.audit.ui;

import java.io.InputStream;
import java.util.Vector;

import obiee.audit.webcat.engine.WebcatAudit;
import obiee.audit.webcat.utils.XMLUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Generates the output files formatting the results of the audit process.
 * @author danielgalassi@gmail.com
 *
 */
public class Publisher {

	private static final Logger logger = LogManager.getLogger(Publisher.class.getName());

	private Vector<InputStream>    stylesheets = new Vector<InputStream>();

	/**
	 * Reads files bundled in the JAR file.
	 * @param rsc name and location of the resource in the JAR file.
	 * @return a stream of the file
	 */
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
	 * Generates XML and HTML privilege audit output files
	 */
	private void publishPrivilegeAuditResults() {
		logger.info("Creating Privilege Audit documentation...");
		XMLUtils.applyStylesheet("Webcat.xml", stylesheets.get(0), "RolesMadeEasy.xml");
		XMLUtils.applyStylesheet("RolesMadeEasy.xml", stylesheets.get(1), "FeaturesByRoleType.html");
		XMLUtils.applyStylesheet("RolesMadeEasy.xml", stylesheets.get(2), "FeaturesByRole.html");
		logger.info("Privilege Audit documentation completed");
	}

	/**
	 * Generates XML and HTML dashboard audit output files
	 */
	private void publishDashboardsAuditResults() {
		logger.info("Creating Dashboard Audit documentation...");
		XMLUtils.applyStylesheet("Webcat.xml", stylesheets.get(3), "RolesMadeEasyForDashboards.xml");
		XMLUtils.applyStylesheet("RolesMadeEasyForDashboards.xml", stylesheets.get(4), "DashboardsByRoleType.html");
		logger.info("Dashboard Audit documentation completed");
	}

	/**
	 * Loads all formatting options
	 */
	private void preparePublisher() {
		stylesheets.add(loadInternalResource("/obiee/audit/bundledApps/RolesMadeEasy.xsl"));
		stylesheets.add(loadInternalResource("/obiee/audit/bundledApps/FeaturesByRoleType.xsl"));
		stylesheets.add(loadInternalResource("/obiee/audit/bundledApps/FeaturesByRole.xsl"));
		stylesheets.add(loadInternalResource("/obiee/audit/bundledApps/RolesMadeEasyForDashboards.xsl"));
		stylesheets.add(loadInternalResource("/obiee/audit/bundledApps/DashboardsByRoleType.xsl"));
	}

	/**
	 * Orchestrates the publication of results
	 */
	public void run() {
		preparePublisher();
		if (WebcatAudit.isPrivilegeAuditInvoked) {
			publishPrivilegeAuditResults();
		}
		if (WebcatAudit.isDashboardAuditInvoked) {
			publishDashboardsAuditResults();
		}
	}
}
