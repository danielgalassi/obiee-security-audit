package webcatAudit;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import webcatSharedObjects.DashboardGroup;
import webcatSystemObjects.Component;
import xmlutils.XMLUtils;

/**
 * 
 * @author danielgalassi@gmail.com
 *
 */
public class WebCatalog {

	private File fWebcat = null;
	public static Document		docWebcat	= XMLUtils.createDOMDocument();
	private static Element		eWebcat		= docWebcat.createElement("WebCat");
	public static Element		eCompList	= docWebcat.createElement("ComponentList");
	public static Element		eDashList	= docWebcat.createElement("DashboardList");
	private Vector <Component>	privs;
	private Vector <DashboardGroup>	dash;

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
				return (f.isDirectory() && f.canRead());
			}
		};

		folderList = getSharedDirectory().listFiles(filter);
		for (int i=0; i<folderList.length; i++)
			dash.add(new DashboardGroup(folderList[i]));

		eWebcat.appendChild(eDashList);
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

	}
}