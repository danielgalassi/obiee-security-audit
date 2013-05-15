package webcatSharedObjects;

import java.io.File;

import org.w3c.dom.Element;

import utils.PrivilegeAttribFile;
import webcatAudit.WebCatalog;

public class DashboardPage {

	private File fPage = null;
	private String sPageName = "";

	public Element serialize() {
		Element eDashboardPage = (WebCatalog.docWebcat).createElement("DashboardPage");
		eDashboardPage.setAttribute("DashboardPageName", sPageName);
		return eDashboardPage;
	}

	public DashboardPage(File file) {
		fPage = file;
		PrivilegeAttribFile pageAttrib = new PrivilegeAttribFile(file+".atr");
		sPageName = pageAttrib.getName();
		System.out.println("\t\t" + sPageName);

	}

}
