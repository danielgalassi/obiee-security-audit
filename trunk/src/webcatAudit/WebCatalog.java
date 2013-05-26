package webcatAudit;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.PrivilegeAttribFile;
import utils.SharedObject;
import utils.XMLUtils;
import webcatSharedObjects.DashboardGroup;
import webcatSharedObjects.Report;
import webcatSystemObjects.ApplicationRole;
import webcatSystemObjects.Component;
import webcatSystemObjects.User;

/**
 * 
 * @author danielgalassi@gmail.com
 *
 */
public class WebCatalog {

	private File fWebcat = null;
	public static Document	docWebcat		= XMLUtils.createDOMDocument();
	private static Element	eWebcat			= docWebcat.createElement("WebCat");
	public static Element	eCompList		= docWebcat.createElement("ComponentList");
	public static Element	eDashGroupList	= docWebcat.createElement("DashboardGroupList");
	private Element 		eAppRoleList	= docWebcat.createElement("ApplicationRoleList");
	private Element			eUserList		= docWebcat.createElement("UserList");
	private Vector <Component>	privs;
	private Vector <DashboardGroup>	dash;
	public static Vector <String> appRoles = new Vector <String> ();
	public static HashMap <String, String> hmAllUsers = new HashMap <String, String> ();
	public static HashMap <String, Report> hmAllReports = new HashMap <String, Report> ();
	public static final Vector <String>		p = new Vector <String> ();
	public static final Vector <Integer>	n = new Vector <Integer> ();

	private void setListOfPermissions() {
		p.add ("Full Control");
		p.add ("View BIPublisher reports");
		p.add ("Schedule BIPublisher reports");
		p.add ("Run BIPublisher reports");
		p.add ("Set Ownership");
		p.add ("Change Permissions");
		p.add ("Modify");
		p.add ("Delete");
		p.add ("Write");
		p.add ("Open");
		p.add ("Traverse");
		p.add ("Read");
		p.add ("No Access");
		n.add (65535);
		n.add (8192);
		n.add (4096);
		n.add (2048);
		n.add (32);
		n.add (16);
		n.add (15);
		n.add (8);
		n.add (4);
		n.add (3);
		n.add (2);
		n.add (1);
		n.add (0);
	}

	private void listAllUsers(File fUsersFolder) {
		FilenameFilter userFoldersOnly = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				File f = new File (dir, name);
				if (f.canRead() && f.isDirectory())
					return true;
				return false;
			}
		};

		FilenameFilter usersOnly = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				File f = new File (dir, name);
				if (f.canRead() && f.isFile() && !name.endsWith(".atr"))
					return true;
				return false;
			}
		};
		File u[] = fUsersFolder.listFiles(userFoldersOnly);
		for (int i=0; i<u.length; i++) {
			File f[] = u[i].listFiles(usersOnly);
			for (int j=0; j<f.length; j++) {
				User usr = new User(f[j]);
				hmAllUsers.put(usr.getID(), usr.getName());
				eUserList.appendChild(usr.serialize());
			}
		}
		eWebcat.appendChild(eUserList);
	}

	private void listAllApplicationRoles (File fAppRolesFolder) {
		FilenameFilter roleFoldersOnly = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				File f = new File (dir, name);
				if (f.canRead() && f.isDirectory())
					return true;
				return false;
			}
		};

		FilenameFilter rolesOnly = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				File f = new File (dir, name);
				if (f.canRead() && f.isFile() && !name.endsWith(".atr"))
					return true;
				return false;
			}
		};
		File r[] = fAppRolesFolder.listFiles(roleFoldersOnly);
		for (int i=0; i<r.length; i++) {
			File f[] = r[i].listFiles(rolesOnly);
			for (int j=0; j<f.length; j++) {
				ApplicationRole ar = new ApplicationRole(f[j]);
				appRoles.add(ar.getName());
				eAppRoleList.appendChild(ar.serialize());
			}
		}
		eWebcat.appendChild(eAppRoleList);
	}

	private void listAllReports (File fSharedFolder, String tab, String unscrambledPath) {
		tab += "\t";
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (!name.endsWith(".atr"))
					return true;
				return false;
			}
		};

		File s[] = fSharedFolder.listFiles(filter);
		for (int i=0; i<s.length; i++) {
			if ((new File(s[i]+".atr").canRead())) {
				PrivilegeAttribFile p = new PrivilegeAttribFile(s[i]+".atr");

				if (s[i].isFile())
					if (SharedObject.isReport(s[i])) {
						Report r = new Report(unscrambledPath, s[i]);
						hmAllReports.put(r.getFullUnscrambledName().replace("–", "-"), r);
					}

				if (s[i].isDirectory())
					listAllReports(s[i], tab, unscrambledPath + "/" + p.getName(true,4));
			}
		}
	}

	/***
	 * 
	 * @return
	 */
	public boolean isValid() {
		return (fWebcat.canRead() && fWebcat.isDirectory());
	}

	/***
	 * 
	 * @return
	 */
	private File getUsersDirectory() {
		File f = new File(fWebcat + "\\root\\system\\security\\users");
		if (!f.canRead() || !f.isDirectory())
			f = null;
		return (f);
	}

	/***
	 * 
	 * @return
	 */
	public File getPrivilegesDirectory() {
		File f = new File(fWebcat + "\\root\\system\\privs");
		if (!f.canRead() || !f.isDirectory())
			f = null;
		return (f);
	}

	/***
	 * 
	 * @return
	 */
	public File getAppRolesDirectory() {
		File f = new File(fWebcat + "\\root\\system\\security\\approles");
		if (!f.canRead() || !f.isDirectory())
			f = null;
		return (f);
	}

	/***
	 * 
	 * @return
	 */
	public File getSharedDirectory() {
		File f = new File(fWebcat + "\\root\\shared");
		if (!f.canRead() || !f.isDirectory())
			f = null;
		return (f);
	}

	/***
	 * 
	 */
	public void processDashboards() {
		File[] folderList = null;
		dash = new Vector <DashboardGroup> ();

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				File f = new File (dir, name + "\\_portal");

				if (f.isDirectory() && f.canRead())
					if (f.listFiles().length>0)
						return true;

				return false;
			}
		};

		folderList = getSharedDirectory().listFiles(filter);
		for (int i=0; i<folderList.length; i++) {
			dash.add(new DashboardGroup(folderList[i]));
			eDashGroupList.appendChild(dash.get(i).serialize());
		}

		eWebcat.appendChild(eDashGroupList);
	}

	/***
	 * 
	 */
	public void processWebCatPrivileges() {
		File[] fList = null;
		privs = new Vector <Component> ();

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if(name.lastIndexOf('.') > 0)
				{
					int lastIndex = name.lastIndexOf('.');
					String str = name.substring(lastIndex);
					if(str.equals(".atr") && name.indexOf("dvt") == -1)
						return true;
				}
				return false;
			}
		};

		fList = getPrivilegesDirectory().listFiles(filter);
		for (int i=0; i<fList.length; i++)
			privs.add(new Component(fList[i]));

		eWebcat.appendChild(eCompList);
	}

	public void save() {
		docWebcat.appendChild(eWebcat);
		XMLUtils.Document2File((WebCatalog.docWebcat), ".\\Webcat.xml");
	}

	/***
	 * 
	 * @param sLocation
	 */
	public WebCatalog(String sLocation) {
		if (!sLocation.isEmpty())
			fWebcat = new File (sLocation);
		eWebcat.setAttribute("app", "obiee-security-audit");
		eWebcat.setAttribute("app-author", "danielgalassi@gmail.com");
		System.out.println("WebCatalog found at " + fWebcat);
		setListOfPermissions();
		System.out.println("Creating an aplication user catalogue");
		listAllUsers(getUsersDirectory());
		System.out.println("Creating an application roles catalogue");
		listAllApplicationRoles(getAppRolesDirectory());
		System.out.println("Creating a reports catalog");
		listAllReports(getSharedDirectory(), "", "/shared");
	}
}
