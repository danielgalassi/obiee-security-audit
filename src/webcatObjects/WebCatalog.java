package webcatObjects;

import java.io.File;
import java.io.FilenameFilter;

public class WebCatalog {

	private File fWebcat;

	/***
	 * 
	 * @return
	 */
	public boolean isValid() {
		return fWebcat.canRead();
	}

	/***
	 * 
	 * @param sLocation
	 */
	public void setLocation(String sLocation) {
		fWebcat = new File (sLocation);
	}

	/***
	 * 
	 * @return
	 */
	public File getPrivilegesDirectory() {
		File f = new File(fWebcat + "\\root\\system\\privs");
		if (!f.canRead())
			f = null;
		return (f);
	}

	/***
	 * 
	 */
	public void processWebCatPrivileges() {
		File[] fList = null;
		PrivilegeSettings privsFile = null;

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if(name.lastIndexOf('.')>0)
				{
					int lastIndex = name.lastIndexOf('.');
					String str = name.substring(lastIndex);
					if(str.equals(".atr") && name.indexOf("dvt") == -1)
						return true;
				}
				return false;
			}
		};

		fList = getPrivilegesDirectory().listFiles(filter);
		//for (int i=0; i<fList.length; i++)
		int i=0;
			privsFile = new PrivilegeSettings(fList[i]);
	}

	/***
	 * 
	 * @param sLocation
	 */
	public WebCatalog(String sLocation) {
		if (!sLocation.isEmpty())
			setLocation(sLocation);
	}
}
