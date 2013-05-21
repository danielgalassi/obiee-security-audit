package webcatAudit;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import utils.PrivilegeAttribFile;
import utils.SharedObject;
import webcatSharedObjects.DashboardGroup;
import webcatSharedObjects.Report;
import webcatSystemObjects.Component;
import xmlutils.XMLUtils;

/**
 * 
 * @author danielgalassi@gmail.com
 *
 */
public class WebCatalog {

	private File fWebcat = null;
	public static Document		docWebcat		= XMLUtils.createDOMDocument();
	private static Element		eWebcat			= docWebcat.createElement("WebCat");
	public static Element		eCompList		= docWebcat.createElement("ComponentList");
	public static Element		eDashGroupList	= docWebcat.createElement("DashboardGroupList");
	private Vector <Component>	privs;
	private Vector <DashboardGroup>	dash;

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
						System.out.print("Unscrambled Path: " + unscrambledPath +"\t");
						new Report(s[i]);
						System.out.println();
					}

				if (s[i].isDirectory())
					listAllReports(s[i], tab, unscrambledPath + "/" + p.getName());
			}
		}
	}

	/***
	 * 
	 * @return
	 */
	public boolean isValid() {
		return fWebcat.canRead();
	}

	/***
	 * 
	 * @return
	 */
	public File getPrivilegesDirectory() {
		File f = new File(fWebcat + "\\root\\system\\privs");
		if (!f.canRead())
			f = null;
		return (f);
	}

	/***
	 * 
	 * @return
	 */
	public File getSharedDirectory() {
		File f = new File(fWebcat + "\\root\\shared");
		if (!f.canRead())
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

		listAllReports(getSharedDirectory(), "", "shared");
	}
}
