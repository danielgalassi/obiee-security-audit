package webcatSharedObjects;

import java.io.File;

import webcatSystemObjects.PrivilegeAttribFile;

public class DashboardGroup {

	private PrivilegeAttribFile groupAttrib = null;
	private String sDashboardGroupName = "";

	public DashboardGroup (File f) {
		if (f.canRead()) {
			groupAttrib = new PrivilegeAttribFile(f.toString()+".atr");

			sDashboardGroupName = groupAttrib.getName();
			System.out.println(sDashboardGroupName);
			/*
			System.out.println(getName());
			fComponentDir = new File(ComponentAttrib.getAttribDir());
			if (!fComponentDir.canRead())
				fComponentDir = null;
			else
				setPrivs();
			 */
		}
	}


}
