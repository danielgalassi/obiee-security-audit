package webcatSharedObjects;

import java.io.File;

public class Dashboard {

	public Dashboard (File f) {
		if (f.canRead()) {
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
