package obiee.audit.webcat.engine;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Vector;

import obiee.audit.webcat.objects.shared.DashboardGroup;
import obiee.audit.webcat.objects.shared.Report;
import obiee.audit.webcat.objects.system.ApplicationRole;
import obiee.audit.webcat.objects.system.Component;
import obiee.audit.webcat.objects.system.User;
import obiee.audit.webcat.utils.PrivilegeAttribFile;
import obiee.audit.webcat.utils.SharedObject;
import obiee.audit.webcat.utils.XMLUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * 
 * @author danielgalassi@gmail.com
 *
 */
public class WebCatalog {

	//private static final Logger logger = LogManager.getLogger(WebCatalog.class.getName());

	private File webcat = null;
	public static Document	docWebcat		= XMLUtils.createDOMDocument();
	private static Element	eWebcat			= docWebcat.createElement("WebCat");
	public static Element	eCompList		= docWebcat.createElement("ComponentList");
	public static Element	eDashGroupList	= docWebcat.createElement("DashboardGroupList");
	private Element 		eAppRoleList	= docWebcat.createElement("ApplicationRoleList");
	private Element			eUserList		= docWebcat.createElement("UserList");
	private Vector <Component>	privs;
	private Vector <DashboardGroup>	dash;
	public static Vector <String> appRoles = new Vector <String> ();
	public static HashMap <String, String> allUsers = new HashMap <String, String> ();
	public static HashMap <String, Report> allReports = new HashMap <String, Report> ();
	public static final Vector <String>		permissions = new Vector <String> ();
	public static final Vector <Integer>	weighingValues = new Vector <Integer> ();

	private void setListOfPermissions() {
		permissions.add ("Full Control");
		permissions.add ("View BIPublisher reports");
		permissions.add ("Schedule BIPublisher reports");
		permissions.add ("Run BIPublisher reports");
		permissions.add ("Set Ownership");
		permissions.add ("Change Permissions");
		permissions.add ("Modify");
		permissions.add ("Delete");
		permissions.add ("Write");
		permissions.add ("Open");
		permissions.add ("Traverse");
		permissions.add ("Read");
		permissions.add ("No Access");
		weighingValues.add (65535);
		weighingValues.add (8192);
		weighingValues.add (4096);
		weighingValues.add (2048);
		weighingValues.add (32);
		weighingValues.add (16);
		weighingValues.add (15);
		weighingValues.add (8);
		weighingValues.add (4);
		weighingValues.add (3);
		weighingValues.add (2);
		weighingValues.add (1);
		weighingValues.add (0);
	}

	private void listAllUsers(File usersFolder) {
		FilenameFilter userFoldersFilter = new FilenameFilter() {
			@Override
			public boolean accept(File directory, String name) {
				File file = new File (directory, name);
				if (file.canRead() && file.isDirectory()) {
					return true;
				}
				return false;
			}
		};

		FilenameFilter usersOnlyFilter = new FilenameFilter() {
			@Override
			public boolean accept(File directory, String name) {
				File file = new File (directory, name);
				if (file.canRead() && file.isFile() && !name.endsWith(".atr")) {
					return true;
				}
				return false;
			}
		};

		for (File userFolder : usersFolder.listFiles(userFoldersFilter))
			for (File file : userFolder.listFiles(usersOnlyFilter)) {
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

		FilenameFilter rolesOnlyFilter = new FilenameFilter() {
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
			for (File roleFile : roles.listFiles(rolesOnlyFilter)) {
				ApplicationRole applicationRole = new ApplicationRole(roleFile);
				appRoles.add(applicationRole.getName());
				eAppRoleList.appendChild(applicationRole.serialize());
			}

		eWebcat.appendChild(eAppRoleList);
	}

	private void listAllReports (File sharedFolder, String tab, String unscrambledPath) {
		tab += "\t";
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (!name.endsWith(".atr"))
					return true;
				return false;
			}
		};

		for (File s : sharedFolder.listFiles(filter))
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

	public boolean isValid() {
		return (webcat.canRead() && webcat.isDirectory());
	}

	private File getUsersDirectory() {
		File usersDir = new File(webcat + "\\root\\system\\security\\users");
		if (!usersDir.canRead() || !usersDir.isDirectory()) {
			usersDir = null;
		}
		return (usersDir);
	}

	public File getPrivilegesDirectory() {
		File privsDir = new File(webcat + "\\root\\system\\privs");
		if (!privsDir.canRead() || !privsDir.isDirectory()) {
			privsDir = null;
		}
		return (privsDir);
	}

	public File getAppRolesDirectory() {
		File rolesDir = new File(webcat + "\\root\\system\\security\\approles");
		if (!rolesDir.canRead() || !rolesDir.isDirectory()) {
			rolesDir = null;
		}
		return (rolesDir);
	}

	public File getSharedDirectory() {
		File sharedDir = new File(webcat + "\\root\\shared");
		if (!sharedDir.canRead() || !sharedDir.isDirectory())
			sharedDir = null;
		return (sharedDir);
	}

	public void processDashboards() {
		dash = new Vector <DashboardGroup> ();

		FilenameFilter dashboardFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				File f = new File (dir, name + "\\_portal");

				if (f.isDirectory() && f.canRead()) {
					if (f.listFiles().length>0) {
						return true;
					}
				}

				return false;
			}
		};

		for (File folder : getSharedDirectory().listFiles(dashboardFilter)) {
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

		for (File privilege : getPrivilegesDirectory().listFiles(filter))
			privs.add(new Component(privilege));

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
//		logger.info("WebCatalog found at {}", webcat);
		setListOfPermissions();
//		logger.info("Creating an aplication user catalogue");
		listAllUsers(getUsersDirectory());
//		logger.info("Creating an application role catalogue");
		listAllApplicationRoles(getAppRolesDirectory());
//		logger.info("Creating a report catalogue");
		listAllReports(getSharedDirectory(), "", "/shared");
	}
}
