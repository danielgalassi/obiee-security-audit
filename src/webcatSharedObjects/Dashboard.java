package webcatSharedObjects;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;

import utils.PrivilegeAttribFile;

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
		for (int i=0; i<pageList.length; i++)
			vPages.add(new DashboardPage(pageList[i]));

		//eWebcat.appendChild(eDashList);
	}

	public Dashboard (File fDashboard) {
		fDashboardDir = fDashboard;
		PrivilegeAttribFile dashboardAttrib = new PrivilegeAttribFile(fDashboardDir+".atr");
		sDashboardName = dashboardAttrib.getName();
		System.out.println("\t" + sDashboardName);

		traversePages();
	}
}
