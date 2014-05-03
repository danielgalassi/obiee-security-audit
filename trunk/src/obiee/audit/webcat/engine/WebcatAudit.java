package obiee.audit.webcat.engine;

import obiee.audit.ui.Publisher;
import obiee.audit.webcat.core.WebCatalog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WebcatAudit {

	private static final Logger logger = LogManager.getLogger(WebcatAudit.class.getName());

	public static boolean     isDashboardAuditInvoked = false;
	public static boolean     isPrivilegeAuditInvoked = false;
	private static Request                    request = null;

	/**
	 * @param args command line options
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
			logger.trace("Initialising Webcat Parsing in progress...");
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

		webcat.export(request.getTargetParam());

		Publisher publisher = new Publisher(request.getTargetParam());
		publisher.run();
	}
}
/*
 * Arguments: -w=path_to_Presentation_Catalogue_folder [-privs -dashboards]
 */
