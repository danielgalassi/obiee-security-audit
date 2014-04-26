package obiee.audit.webcat.core;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Vector;

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
	private static Element	eWebcat			= docWebcat.createElement("WebCat");
	public static Element	eCompList		= docWebcat.createElement("ComponentList");
	public static Element	eDashGroupList	= docWebcat.createElement("DashboardGroupList");
	private Element 		eAppRoleList	= docWebcat.createElement("ApplicationRoleList");
	private Element			eUserList		= docWebcat.createElement("UserList");

	private Vector <Component>				privs;
	private Vector <DashboardGroup>			dash;

	public static Vector <String>			appRoles = new Vector <String> ();
	public static HashMap <String, String>	allUsers = new HashMap <String, String> ();
	public static HashMap <String, Report>	allReports = new HashMap <String, Report> ();
	public static final StandardSecuritySettings ootbSecurity = new StandardSecuritySettings();

	private void listAllUsers(File usersFolder) {
		FilenameFilter userFolders = new FilenameFilter() {
			@Override
			public boolean accept(File directory, String name) {
				File file = new File (directory, name);
				if (file.canRead() && file.isDirectory()) {
					return true;
				}
				return false;
			}
		};

		FilenameFilter usersOnly = new FilenameFilter() {
			@Override
			public boolean accept(File directory, String name) {
				File file = new File (directory, name);
				if (file.canRead() && file.isFile() && !name.endsWith(".atr")) {
					return true;
				}
				return false;
			}
		};

		for (File userFolder : usersFolder.listFiles(userFolders))
			for (File file : userFolder.listFiles(usersOnly)) {
				User user = new User(file);
				allUsers.put(user.getID(), user.getName());
				eUserList.appendChild(user.serialize());
			}

		eWebcat.appendChild(eUserList);
	}

	private void listAllApplicationRoles (File appRolesFolder) {
		FilenameFilter roleFoldersFilter = new FilenameFilter() {
			@Override
			public boolean accept(File directory, String name) {
				File file = new File (directory, name);
				if (file.canRead() && file.isDirectory())
					return true;
				return false;
			}
		};

		FilenameFilter rolesOnly = new FilenameFilter() {
			@Override
			public boolean accept(File directory, String name) {
				File file = new File (directory, name);
				if (file.canRead() && file.isFile() && !name.endsWith(".atr")) {
					return true;
				}
				return false;
			}
		};

		for (File roles : appRolesFolder.listFiles(roleFoldersFilter))
			for (File roleFile : roles.listFiles(rolesOnly)) {
				ApplicationRole applicationRole = new ApplicationRole(roleFile);
				appRoles.add(applicationRole.getName());
				eAppRoleList.appendChild(applicationRole.serialize());
			}

		eWebcat.appendChild(eAppRoleList);
	}

	private void listAllReports (File sharedFolder, String tab, String unscrambledPath) {
		tab += "\t";
		FilenameFilter excludingAttributes = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (!name.endsWith(".atr"))
					return true;
				return false;
			}
		};

		for (File s : sharedFolder.listFiles(excludingAttributes)) {
			if ((new File(s+".atr").canRead())) {
				PrivilegeAttribFile privilege = new PrivilegeAttribFile(s+".atr");

				if (s.isFile())
					if (SharedObject.isReport(s)) {
						Report r = new Report(unscrambledPath, s);
						allReports.put(r.getFullUnscrambledName().replace("–", "-"), r);
					}

				if (s.isDirectory()) {
					listAllReports(s, tab, unscrambledPath + "/" + privilege.getName(true,4));
				}
			}
		}
	}

//	public boolean isValid() {
//		return (webcat.canRead() && webcat.isDirectory());
//	}

	private File getDirectory(String type) {
		File dir = new File (webcat + type);
		
		if (!(dir.canRead() && dir.isDirectory())) {
			dir = null;
		}
		return dir;
	}

	public void processDashboards() {
		dash = new Vector <DashboardGroup> ();

		FilenameFilter dashboardFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				File f = new File (dir, name + "\\_portal");

				if (f.isDirectory() && f.canRead()) {
					if (f.listFiles().length > 0) {
						return true;
					}
				}

				return false;
			}
		};

		for (File folder : getDirectory(sharedPath).listFiles(dashboardFilter)) {
			DashboardGroup dg = new DashboardGroup(folder);
			dash.add(dg);
			eDashGroupList.appendChild(dg.serialize());
		}

		eWebcat.appendChild(eDashGroupList);
	}

	/***
	 * 
	 */
	public void processWebCatPrivileges() {
		privs = new Vector <Component> ();

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if(name.lastIndexOf('.') > 0) {
					int lastIndex = name.lastIndexOf('.');
					String str = name.substring(lastIndex);
					if(str.equals(".atr") && name.indexOf("dvt") == -1) {
						return true;
					}
				}
				return false;
			}
		};

		for (File privilege : getDirectory(privsPath).listFiles(filter)) {
			privs.add(new Component(privilege));
		}

		eWebcat.appendChild(eCompList);
	}

	public void save() {
		docWebcat.appendChild(eWebcat);
		XMLUtils.saveDocument((WebCatalog.docWebcat), ".\\Webcat.xml");
	}

	/***
	 * 
	 * @param location
	 */
	public WebCatalog(String location) {
		if (!location.isEmpty()) {
			webcat = new File (location);
		}

		eWebcat.setAttribute("app", "obiee-security-audit");
		eWebcat.setAttribute("app-author", "danielgalassi@gmail.com");

		logger.info("WebCatalog found at {}", webcat);
		logger.info("Creating an aplication user catalogue");
		listAllUsers(getDirectory(usersPath));
		logger.info("Creating an application role catalogue");
		listAllApplicationRoles(getDirectory(rolesPath));
		logger.info("Creating a report catalogue");
		listAllReports(getDirectory(sharedPath), "", "/shared");
	}
}
