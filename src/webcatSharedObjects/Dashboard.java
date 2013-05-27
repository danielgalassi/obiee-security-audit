package webcatSharedObjects;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import utils.PrivilegeAttribFile;
import utils.SharedObject;
import utils.XMLUtils;
import webcatAudit.WebCatalog;

public class Dashboard {

	private File fDashboardDir = null;
	private boolean isOOTB = false;
	private String sDashboardName = "";
	private Vector <DashboardPage> vPages;
	private Vector <Permission> vPerms;

	private void getPageAttributes(String tag) {
		File fDashLayout = new File(fDashboardDir+"\\dashboard+layout");
		if (fDashLayout.canRead()) {
			Document dashLayoutDOM = XMLUtils.File2Document(fDashLayout);
			XPath xPath = XPathFactory.newInstance().newXPath();

			try {
				Node nTag = (Node) xPath.evaluate(tag,
						dashLayoutDOM.getDocumentElement(),
						XPathConstants.NODE);
				if (nTag != null)
					isOOTB = true;
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}
	}

	private void traversePages() {
		vPages = new Vector <DashboardPage> ();

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (!name.endsWith(".atr") && !name.equals("dashboard+layout")) 
					return true;

				return false;
			}
		};

		for (File page : fDashboardDir.listFiles(filter))
			vPages.add(new DashboardPage(page));

	}

	public Element serialize() {
		Element eDashboardPageList = (WebCatalog.docWebcat).createElement("DashboardPageList");
		Element eDashboard = (WebCatalog.docWebcat).createElement("Dashboard");
		eDashboard.setAttribute("DashboardName", sDashboardName);
		eDashboard.setAttribute("isOOTB", isOOTB+"");

		for (DashboardPage page : vPages)
			eDashboardPageList.appendChild(page.serialize());

		Element ePermissionList = (WebCatalog.docWebcat).createElement("PermissionList");

		for (Permission p : vPerms)
			ePermissionList.appendChild(p.serialize());

		eDashboard.appendChild(ePermissionList);

		eDashboard.appendChild(eDashboardPageList);
		return eDashboard;
	}

	public Dashboard (File fDashboard) {
		fDashboardDir = fDashboard;
		PrivilegeAttribFile dashboardAttrib = new PrivilegeAttribFile(fDashboardDir+".atr");
		sDashboardName = dashboardAttrib.getName(true,4);
		System.out.println("\tDashboard: " + sDashboardName);

		traversePages();
		getPageAttributes("/dashboard/@appObjectID");
		vPerms = new Vector <Permission> ();
		vPerms = (SharedObject.getPrivileges(fDashboard));
	}
}
