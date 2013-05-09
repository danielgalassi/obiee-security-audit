package webcatObjects;

import java.io.File;
import java.io.FilenameFilter;

public class WebCatalog {

	File fWebcat;

	public boolean isValid() {
		return fWebcat.canRead();
	}

	public void setLocation(String sLocation) {
		fWebcat = new File (sLocation);
	}

	public File getPrivilegesDirectory() {
		File f = new File(fWebcat + "\\root\\system\\privs");
		if (!f.canRead())
			f = null;
		return (f);
	}

	public void processWebCatPrivileges() {
		File[] fList = null;
		PrivilegeSettingsFile privsFile = null;
		
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if(name.lastIndexOf('.')>0)
				{
					int lastIndex = name.lastIndexOf('.');
					String str = name.substring(lastIndex);
					if(str.equals(".atr"))
						return true;
				}
				return false;
			}
		};

		System.out.println("Only .atr (Privs) files...");
		fList = getPrivilegesDirectory().listFiles(filter);
		for (int i=0; i<fList.length; i++) {
			privsFile = new PrivilegeSettingsFile(fList[i]);
			System.out.println(privsFile.getUnscrambledName());
		}
	}

	public WebCatalog(String sLocation) {
		if (!sLocation.isEmpty())
			setLocation(sLocation);
	}
}
