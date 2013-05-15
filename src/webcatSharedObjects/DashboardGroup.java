package webcatSharedObjects;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;

import utils.PrivilegeAttribFile;

public class DashboardGroup {

	private PrivilegeAttribFile groupAttrib = null;
	private String sDashboardGroupName = "";
	private File fDashboardDir = null;
	private Vector <Dashboard> vDashboards;


	private void setDashboards() {
		File[] dashboardList = null;
		vDashboards = new Vector <Dashboard> ();
		
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				System.out.println(new File(dir, name));
				if(name.lastIndexOf('.')>0)
				{
					int lastIndex = name.lastIndexOf('.');
					String str = name.substring(lastIndex);
					//selecting only attribute files
					if(str.equals(".atr") && 
						name.indexOf("dvt") == -1 && 
						!name.startsWith("_"))
						return true;
				}
				return false;
			}
		};

		System.out.println(sDashboardGroupName);
		dashboardList = fDashboardDir.listFiles(filter);
		for (int i=0; i<dashboardList.length; i++)
			vDashboards.add(new Dashboard(dashboardList[i]));

		//eWebcat.appendChild(eDashList);
	}

	/***
	 * Returns the name of this Dashboard Group.
	 * @return the group name.
	 */
	public String getName () {
		return sDashboardGroupName;
	}

	public DashboardGroup (File f) {
		if (f.canRead()) {
			fDashboardDir = f;
			groupAttrib = new PrivilegeAttribFile(fDashboardDir+".atr");

			sDashboardGroupName = groupAttrib.getName();
			setDashboards();
		}
	}

}
