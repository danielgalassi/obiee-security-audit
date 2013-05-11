package webcatObjects;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import xmlutils.XMLUtils;

public class WebCatalog {

	private File fWebcat;
	protected static Document docWebcat = XMLUtils.createDOMDocument();
	protected static Element eWebcat = docWebcat.createElement("WebCat");
	private Vector <PrivilegeSettings> privs;

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
		privs = new Vector <PrivilegeSettings> ();

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if(name.lastIndexOf('.') > 0)
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
		for (int i=0; i<fList.length; i++)
			privs.add(new PrivilegeSettings(fList[i]));

		System.out.println("Final # of children nodes: " + eWebcat.getChildNodes().getLength());
		docWebcat.appendChild(eWebcat);
		XMLUtils.Document2File((WebCatalog.docWebcat), ".\\Webcat.xml");
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
