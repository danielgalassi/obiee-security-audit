package webcatSharedObjects;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;

import org.w3c.dom.Element;

import utils.PrivilegeAttribFile;
import webcatAudit.WebCatalog;

public class Dashboard {

	private File fDashboardDir = null;
	private String sDashboardName = "";
	private Vector <DashboardPage> vPages;

	private void traversePages() {
		File[] pageList = null;
		vPages = new Vector <DashboardPage> ();

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (!name.endsWith(".atr") && !name.equals("dashboard+layout")) 
					return true;

				return false;
			}
		};

		pageList = fDashboardDir.listFiles(filter);
		Element e = serialize();
		for (int i=0; i<pageList.length; i++) {
			vPages.add(new DashboardPage(pageList[i]));
			//e.appendChild(vPages.get(i).serialize());
			//(WebCatalog.eDashGroupList).appendChild(e);
		}
	}

	public Element serialize() {
		Element eDashboard = (WebCatalog.docWebcat).createElement("Dashboard");
		eDashboard.setAttribute("DashboardName", sDashboardName);
		return eDashboard;
	}

	public Dashboard (File fDashboard) {
		fDashboardDir = fDashboard;
		PrivilegeAttribFile dashboardAttrib = new PrivilegeAttribFile(fDashboardDir+".atr");
		sDashboardName = dashboardAttrib.getName();
		System.out.println("\t" + sDashboardName);

		traversePages();
	}
}
