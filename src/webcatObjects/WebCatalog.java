package webcatObjects;

import java.io.File;

public class WebCatalog {

	File fWebcat;

	public boolean isValid() {
		return fWebcat.canRead();
	}

	public WebCatalog(String sLocation) {
		if (!sLocation.isEmpty())
			setLocation(sLocation);
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
}
