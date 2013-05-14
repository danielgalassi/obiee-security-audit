package webcatSystemObjects;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import xmlutils.XMLUtils;

/**
 * 
 * @author danielgalassi@gmail.com
 *
 */
public class WebCatalog {

	private File fWebcat = null;
	protected static Document docWebcat = XMLUtils.createDOMDocument();
	protected static Element eWebcat = docWebcat.createElement("WebCat");
	private Vector <Component> privs;

	/***
	 * 
	 * @return
	 */
	public boolean isValid() {
		return fWebcat.canRead();
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
		privs = new Vector <Component> ();

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
			privs.add(new Component(fList[i]));

		eWebcat.setAttribute("app", "obiee-security-audit");
		eWebcat.setAttribute("app-author", "danielgalassi@gmail.com");
		docWebcat.appendChild(eWebcat);
		XMLUtils.Document2File((WebCatalog.docWebcat), ".\\Webcat.xml");
	}

	/***
	 * 
	 * @param sLocation
	 */
	public WebCatalog(String sLocation) {
		if (!sLocation.isEmpty())
			fWebcat = new File (sLocation);
	}
}
