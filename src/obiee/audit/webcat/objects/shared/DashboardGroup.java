package obiee.audit.webcat.objects.shared;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;

import obiee.audit.webcat.core.PrivilegeAttribFile;
import obiee.audit.webcat.core.WebCatalog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;


public class DashboardGroup {

	private static final Logger logger = LogManager.getLogger(DashboardGroup.class.getName());

	private PrivilegeAttribFile       groupAttrib = null;
	private String                           name = "";
	private File                dashboardGroupDir = null;
	private Vector <Dashboard>         dashboards;

	private void traverseDashboards() {
		logger.trace("Traversing dashboards...");
		File portal = new File (dashboardGroupDir + "\\_portal");
		dashboards = new Vector <Dashboard> ();

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				File dashboard = null;
				if (!name.endsWith(".atr")) {
					dashboard = new File(dir+"\\"+name+"\\dashboard+layout");
					if(dashboard.canRead() && dashboard.isFile()) {
						return true;
					}
				}
				return false;
			}
		};

		for (File dashboard : portal.listFiles(filter)) {
			dashboards.add(new Dashboard(dashboard));
		}
	}

	/***
	 * Returns the name of this Dashboard Group.
	 * @return the group name.
	 */
	public String getName() {
		return name;
	}

	public Element serialize() {
		Element dashboardGroup = (WebCatalog.docWebcat).createElement("DashboardGroup");
		Element dashboardList = (WebCatalog.docWebcat).createElement("DashboardList");
		dashboardGroup.setAttribute("DashboardGroupName", name);

		for (Dashboard dashboard : dashboards) {
			dashboardList.appendChild(dashboard.serialize());
		}

		dashboardGroup.appendChild(dashboardList);

		return (dashboardGroup);
	}

	public DashboardGroup (File dashboardGroupFolder) {
		if (dashboardGroupFolder.canRead()) {
			dashboardGroupDir = dashboardGroupFolder;
			groupAttrib = new PrivilegeAttribFile(dashboardGroupDir+".atr");
			name = groupAttrib.getName(true, 4);
			traverseDashboards();
		}
	}

}
