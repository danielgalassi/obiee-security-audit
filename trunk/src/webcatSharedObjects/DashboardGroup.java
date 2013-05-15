package webcatSharedObjects;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;

import org.w3c.dom.Element;

import utils.PrivilegeAttribFile;
import webcatAudit.WebCatalog;

public class DashboardGroup {

	private PrivilegeAttribFile groupAttrib = null;
	private String sDashboardGroupName = "";
	private File fDashboardGroupDir = null;
	private Vector <Dashboard> vDashboards;


	private void traverseDashboards() {
		File portal = new File (fDashboardGroupDir+"\\_portal");
		File[] dashboardList = null;
		vDashboards = new Vector <Dashboard> ();

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				File dashboard = null;
				if (!name.endsWith(".atr")) {
					dashboard = new File(dir+"\\"+name+"\\dashboard+layout");
					if(dashboard.canRead() && dashboard.isFile())
						return true;
				}
				return false;
			}
		};

		dashboardList = portal.listFiles(filter);
		for (int i=0; i<dashboardList.length; i++)
			vDashboards.add(new Dashboard(dashboardList[i]));
	}

	/***
	 * Returns the name of this Dashboard Group.
	 * @return the group name.
	 */
	public String getName() {
		return sDashboardGroupName;
	}

	public Element serialize() {
		Element eGroup = (WebCatalog.docWebcat).createElement("DashboardGroup");
		Element eDashList = (WebCatalog.docWebcat).createElement("DashboardList");
		eGroup.setAttribute("DashboardGroupName", sDashboardGroupName);

		for (int i=0; i<vDashboards.size(); i++)
			eDashList.appendChild(vDashboards.get(i).serialize());

		eGroup.appendChild(eDashList);

		return (eGroup);
	}

	public DashboardGroup (File f) {
		if (f.canRead()) {
			fDashboardGroupDir = f;
			groupAttrib = new PrivilegeAttribFile(fDashboardGroupDir+".atr");

			sDashboardGroupName = groupAttrib.getName();
			System.out.println(sDashboardGroupName);
			traverseDashboards();
		}
	}

}
