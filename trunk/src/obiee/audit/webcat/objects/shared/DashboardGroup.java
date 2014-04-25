package obiee.audit.webcat.objects.shared;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;

import obiee.audit.webcat.engine.WebCatalog;
import obiee.audit.webcat.utils.PrivilegeAttribFile;

import org.w3c.dom.Element;


public class DashboardGroup {

	private PrivilegeAttribFile groupAttrib = null;
	private String sDashboardGroupName = "";
	private File fDashboardGroupDir = null;
	private Vector <Dashboard> vDashboards;

	private void traverseDashboards() {
		File portal = new File (fDashboardGroupDir+"\\_portal");
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

		for (File dashboard : portal.listFiles(filter))
			vDashboards.add(new Dashboard(dashboard));
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

		for (Dashboard dashboard : vDashboards)
			eDashList.appendChild(dashboard.serialize());

		eGroup.appendChild(eDashList);

		return (eGroup);
	}

	public DashboardGroup (File f) {
		if (f.canRead()) {
			fDashboardGroupDir = f;
			groupAttrib = new PrivilegeAttribFile(fDashboardGroupDir+".atr");

			sDashboardGroupName = groupAttrib.getName(true,4);
			//System.out.println("Group: " + sDashboardGroupName);
			traverseDashboards();
		}
	}

}
