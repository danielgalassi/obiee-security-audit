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
import obiee.audit.webcat.objects.system.User;
import obiee.audit.webcat.utils.XMLUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;



/**
 * 
 * @author danielgalassi@gmail.com
 *
 */
public class WebCatalog {

	private static final Logger logger = LogManager.getLogger(WebCatalog.class.getName());

	private static final String sharedPath = "\\root\\shared";
	private static final String privsPath = "\\root\\system\\privs";
	private static final String usersPath = "\\root\\system\\security\\users";
	private static final String rolesPath = "\\root\\system\\security\\approles";

	private static File		webcat			= null;
	public static Document	docWebcat		= XMLUtils.createDOMDocument();
	public static Element	eCompList		= docWebcat.createElement("ComponentList");
	public static Element	eDashGroupList	= docWebcat.createElement("DashboardGroupList");
	private static Element	eWebcat			= docWebcat.createElement("WebCat");
	private Element 		roleList		= docWebcat.createElement("ApplicationRoleList");
	private Element			userList		= docWebcat.createElement("UserList");

	private Vector <Component>				privs;
	private Vector <DashboardGroup>			dash;

	public static Vector <String>			appRoles = new Vector <String> ();
	public static HashMap <String, String>	allUsers = new HashMap <String, String> ();
	public static HashMap <String, Report>	allReports = new HashMap <String, Report> ();
	public static final StandardSecuritySettings ootbSecurity = new StandardSecuritySettings();

	private void examineUsers(File usersFolder) {
		logger.info("Creating an aplication user catalogue");

		for (File userFolder : usersFolder.listFiles(new FolderFilter())) {
			for (File file : userFolder.listFiles(new ExcludeAttributeFileFilter())) {
				User user = new User(file);
				allUsers.put(user.getID(), user.getName());
				userList.appendChild(user.serialize());
			}
		}

		eWebcat.appendChild(userList);
	}

	private void examineRoles (File appRolesFolder) {
		logger.info("Creating an application role catalogue");

		for (File roles : appRolesFolder.listFiles(new FolderFilter())) {
			for (File roleFile : roles.listFiles(new ExcludeAttributeFileFilter())) {
				ApplicationRole applicationRole = new ApplicationRole(roleFile);
				appRoles.add(applicationRole.getName());
				roleList.appendChild(applicationRole.serialize());
			}
		}

		eWebcat.appendChild(roleList);
	}

	private void examineReports (File sharedFolder, String path) {
		for (File s : sharedFolder.listFiles(new ExcludeAttributeObjectFilter())) {
			if ((new File(s+".atr").canRead())) {
				PrivilegeAttribFile privilege = new PrivilegeAttribFile(s+".atr");

				if (s.isFile()) {
					if (SharedObject.isReport(s)) {
						Report r = new Report(path, s);
						allReports.put(r.getFullUnscrambledName().replace("–", "-"), r);
					}
				}

				if (s.isDirectory()) {
					examineReports(s, path + "/" + privilege.getName(true,4));
				}
			}
		}
	}

	private File getDirectory(String type) {
		File dir = new File (webcat + type);

		if (!(dir.canRead() && dir.isDirectory())) {
			dir = null;
		}

		return dir;
	}

	public void processDashboards() {
		dash = new Vector <DashboardGroup> ();

		for (File folder : getDirectory(sharedPath).listFiles(new DashboardFilter())) {
			DashboardGroup dashboardGroup = new DashboardGroup(folder);
			dash.add(dashboardGroup);
			eDashGroupList.appendChild(dashboardGroup.serialize());
		}

		eWebcat.appendChild(eDashGroupList);
	}

	/***
	 * 
	 */
	public void processWebCatPrivileges() {
		privs = new Vector <Component> ();

		for (File privilege : getDirectory(privsPath).listFiles(new AttributeFileFilter())) {
			privs.add(new Component(privilege));
		}

		eWebcat.appendChild(eCompList);
	}

	public void export() {
		docWebcat.appendChild(eWebcat);
		XMLUtils.saveDocument(docWebcat, ".\\Webcat.xml");
	}

	/***
	 * 
	 * @param location
	 * @throws FileNotFoundException 
	 */
	public WebCatalog(String location) throws FileNotFoundException {

		webcat = new File (location);
		if (!(webcat.exists() && webcat.canRead() && webcat.isDirectory()) || location.isEmpty()) {
			logger.fatal("Webcat could not be opened");
			throw new FileNotFoundException();
		}

		eWebcat.setAttribute("app", "obiee-security-audit");
		eWebcat.setAttribute("app-author", "danielgalassi@gmail.com");
		logger.info("WebCatalog found at {}", webcat);

		examineUsers(getDirectory(usersPath));

		examineRoles(getDirectory(rolesPath));

		logger.info("Creating a report catalogue");
		examineReports(getDirectory(sharedPath), "/shared");
	}
}
