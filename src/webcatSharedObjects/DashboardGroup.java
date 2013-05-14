package webcatSharedObjects;

import java.io.File;

public class DashboardGroup {

	private String sDashboardGroup = "";


	public DashboardGroup (File f) {
		if (f.canRead()) {
			System.out.println(f.toString()+".atr");

			
			/*
			ComponentAttrib = new PrivilegeAttribFile(f.toString());

			sPrivGroupName = ComponentAttrib.getName();
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
