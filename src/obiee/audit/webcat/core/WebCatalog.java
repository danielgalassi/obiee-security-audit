package obiee.audit.webcat.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Vector;

import obiee.audit.webcat.filters.AttributeFileFilter;
import obiee.audit.webcat.filters.DashboardFilter;
import obiee.audit.webcat.filters.ExcludeAttributeFileFilter;
import obiee.audit.webcat.filters.ExcludeAttributeObjectFilter;
import obiee.audit.webcat.filters.FolderFilter;
import obiee.audit.webcat.objects.shared.DashboardGroup;
import obiee.audit.webcat.objects.shared.Report;
import obiee.audit.webcat.objects.system.ApplicationRole;
import obiee.audit.webcat.objects.system.Component;
import obiee.audit.webcat.objects.system.OBIAccount;
import obiee.audit.webcat.objects.system.RemovedUser;
import obiee.audit.webcat.objects.system.User;
import obiee.audit.webcat.utils.XMLUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;



/**
 * This class represents an OBIEE Presentation Catalogue or webcat for short.
 * A webcat is a filesystem-based, multi-layered directory structure that serves as the repository for dashboards and reports.
 * Authorisation aspects of an OBIEE implementation such as the roles or users granted access to features or dashboards are also stored within the webcat.
 * @author danielgalassi@gmail.com
 *
 */
public class WebCatalog {

	private static final Logger logger = LogManager.getLogger(WebCatalog.class.getName());

	/** the shared folder where non-personal dashboards and reports are found */
	private static final String                    sharedPath = "\\root\\shared";
	/** the folder where out-of-the-box OBIEE privileges are defined */
	private static final String                     privsPath = "\\root\\system\\privs";
	/** the folder where OBIEE application roles are defined*/
	private static final String                     rolesPath = "\\root\\system\\security\\approles";
	/** the folder where active user accounts are found */
	private static final String                     usersPath = "\\root\\system\\security\\users";
	/** the folder where no-longer existing accounts are moved to (if they are still identified as presentation object owners) */
	private static final String              removedUsersPath = "\\root\\system\\security\\recoverguids";

	private static File                            webcatFile = null;
	public static Document                          docWebcat = XMLUtils.createDOMDocument();
	public static Element                      componentsList = docWebcat.createElement("ComponentList");
	public static Element                  dashboardGroupList = docWebcat.createElement("DashboardGroupList");
	private static Element                             webcat = docWebcat.createElement("WebCat");
	private Element                                  roleList = docWebcat.createElement("ApplicationRoleList");
	private Element                                  userList = docWebcat.createElement("UserList");

	public static Vector <String>                    appRoles = new Vector <String>();
	public static HashMap <String, String>              users = new HashMap <String, String>();
	public static HashMap <String, Report>            reports = new HashMap <String, Report>();
	public static final StandardSecuritySettings ootbSecurity = new StandardSecuritySettings();

	private Vector <Component>                          privs;
	private Vector <DashboardGroup>           dashboardGroups;

	private void examineAccounts() {
		logger.info("Creating an aplication user catalogue");
		examineUsers(getDirectory(usersPath), "user");
		examineUsers(getDirectory(removedUsersPath), "removed user");
		webcat.appendChild(userList);
	}

	/**
	 * 
	 * @param usersFolder
	 */
	private void examineUsers(File usersFolder, String type) {
		for (File userFolder : usersFolder.listFiles(new FolderFilter())) {
			for (File userFile : userFolder.listFiles(new ExcludeAttributeFileFilter())) {
				OBIAccount user = null;

				switch (type) {
				case "user":
					user = new User(userFile);
					break;
				case "removed user":
					user = new RemovedUser(userFile);
					break;
				default:
					break;
				}

				users.put(user.getID(), user.getName());
				userList.appendChild(user.serialize());
			}
		}
	}

	/***
	 * 
	 * @param appRolesFolder
	 */
	private void examineRoles (File appRolesFolder) {
		logger.info("Creating an application role catalogue");

		for (File roles : appRolesFolder.listFiles(new FolderFilter())) {
			for (File roleFile : roles.listFiles(new ExcludeAttributeFileFilter())) {
				ApplicationRole applicationRole = new ApplicationRole(roleFile);
				appRoles.add(applicationRole.getName());
				roleList.appendChild(applicationRole.serialize());
			}
		}

		webcat.appendChild(roleList);
	}

	/***
	 * 
	 * @param sharedFolder
	 * @param path
	 */
	private void examineReports (File sharedFolder, String path) {
		for (File sharedObject : sharedFolder.listFiles(new ExcludeAttributeObjectFilter())) {
			if ((new File(sharedObject+".atr").canRead())) {
				PrivilegeAttribFile privilege = new PrivilegeAttribFile(sharedObject+".atr");

				if (sharedObject.isFile()) {
					if (SharedObject.isReport(sharedObject)) {
						Report report = new Report(path, sharedObject);
						reports.put(report.getFullUnscrambledName().replace("–", "-"), report);
					}
				}

				if (sharedObject.isDirectory()) {
					examineReports(sharedObject, path + "/" + privilege.getName(true, 4));
				}
			}
		}
	}

	/***
	 * 
	 * @param file a file in the presentation catalogue folder
	 * @return a web catalogue directory
	 */
	private File getDirectory(String file) {
		File dir = new File (webcatFile + file);

		if (!(dir.canRead() && dir.isDirectory())) {
			dir = null;
		}

		return dir;
	}

	/***
	 * 
	 */
	public void auditDashboards() {
		logger.info("Dashboard audit in progress...");

		dashboardGroups = new Vector <DashboardGroup> ();
		for (File folder : getDirectory(sharedPath).listFiles(new DashboardFilter())) {
			DashboardGroup dashboardGroup = new DashboardGroup(folder);
			dashboardGroups.add(dashboardGroup);
			dashboardGroupList.appendChild(dashboardGroup.serialize());
		}
		webcat.appendChild(dashboardGroupList);

		logger.info("Dashboard Audit completed");
	}

	/***
	 * 
	 */
	public void auditPrivileges() {
		logger.info("Privilege audit in progress...");

		privs = new Vector <Component> ();

		for (File privilege : getDirectory(privsPath).listFiles(new AttributeFileFilter())) {
			privs.add(new Component(privilege));
		}
		webcat.appendChild(componentsList);

		logger.info("Privilege audit completed");
	}

	/***
	 * 
	 */
	public void export() {
		docWebcat.appendChild(webcat);
		XMLUtils.saveDocument(docWebcat, ".\\Webcat.xml");
	}

	/***
	 * 
	 * @param location
	 * @throws FileNotFoundException 
	 */
	public WebCatalog(String location) throws FileNotFoundException {

		webcatFile = new File (location);
		if (!(webcatFile.exists() && webcatFile.canRead() && webcatFile.isDirectory()) || location.isEmpty()) {
			logger.fatal("Webcat could not be opened");
			throw new FileNotFoundException();
		}

		webcat.setAttribute("app", "obiee-security-audit");
		webcat.setAttribute("app-author", "danielgalassi@gmail.com");
		logger.info("WebCatalog found at {}", webcatFile);

		examineAccounts();
		examineRoles(getDirectory(rolesPath));

		logger.info("Creating a report catalogue");
		examineReports(getDirectory(sharedPath), "/shared");
	}
}
