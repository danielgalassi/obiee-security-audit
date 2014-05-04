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
	private String                   targetDir;

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
		logger.trace("Creating Privilege Audit documentation...");
		XMLUtils.applyStylesheet(targetDir+"Webcat.xml", stylesheets.get(0), targetDir+"RolesMadeEasy.xml");
		XMLUtils.applyStylesheet(targetDir+"RolesMadeEasy.xml", stylesheets.get(1), targetDir+"FeaturesByRoleType.html");
		XMLUtils.applyStylesheet(targetDir+"RolesMadeEasy.xml", stylesheets.get(2), targetDir+"FeaturesByRole.html");
		logger.trace("Privilege Audit documentation completed");
	}

	/**
	 * Generates XML and HTML dashboard audit output files
	 */
	private void publishDashboardsAuditResults() {
		logger.trace("Creating Dashboard Audit documentation...");
		XMLUtils.applyStylesheet(targetDir+"Webcat.xml", stylesheets.get(3), targetDir+"RolesMadeEasyForDashboards.xml");
		XMLUtils.applyStylesheet(targetDir+"RolesMadeEasyForDashboards.xml", stylesheets.get(4), targetDir+"DashboardsByRoleType.html");
		logger.trace("Dashboard Audit documentation completed");
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
	
	public Publisher(String targetDir) {
		this.targetDir = targetDir;
		logger.info("Saving results to {}", targetDir);
	}
}
