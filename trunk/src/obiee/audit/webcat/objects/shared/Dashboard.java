package obiee.audit.webcat.objects.shared;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import obiee.audit.webcat.engine.WebCatalog;
import obiee.audit.webcat.utils.PrivilegeAttribFile;
import obiee.audit.webcat.utils.SharedObject;
import obiee.audit.webcat.utils.XMLUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class Dashboard {

	private static final Logger logger = LogManager.getLogger(Dashboard.class.getName());

	private File dashboardDir = null;
	private boolean isOOTB = false;
	private String name = "";
	private Vector <DashboardPage> pages;
	private Vector <Permission> permissions;

	private void getPageAttributes(String tag) {
		File fDashLayout = new File(dashboardDir+"\\dashboard+layout");
		if (fDashLayout.canRead()) {
			Document dashLayoutDOM = XMLUtils.loadDocument(fDashLayout);
			XPath xPath = XPathFactory.newInstance().newXPath();

			try {
				Node nTag = (Node) xPath.evaluate(tag,
						dashLayoutDOM.getDocumentElement(),
						XPathConstants.NODE);
				if (nTag != null)
					isOOTB = true;
			} catch (XPathExpressionException e) {
				logger.error("{} thrown while attempting to get Dashboard Page attributes", e.getClass().getCanonicalName());
			}
		}
	}

	private void traversePages() {
		pages = new Vector <DashboardPage> ();

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (!name.endsWith(".atr") && !name.equals("dashboard+layout")) 
					return true;

				return false;
			}
		};

		for (File page : dashboardDir.listFiles(filter)) {
			pages.add(new DashboardPage(page));
		}

	}

	public Element serialize() {
		Element eDashboardPageList = (WebCatalog.docWebcat).createElement("DashboardPageList");
		Element eDashboard = (WebCatalog.docWebcat).createElement("Dashboard");
		eDashboard.setAttribute("DashboardName", name);
		eDashboard.setAttribute("isOOTB", isOOTB+"");

		for (DashboardPage page : pages)
			eDashboardPageList.appendChild(page.serialize());

		Element ePermissionList = (WebCatalog.docWebcat).createElement("PermissionList");

		for (Permission p : permissions)
			ePermissionList.appendChild(p.serialize());

		eDashboard.appendChild(ePermissionList);

		eDashboard.appendChild(eDashboardPageList);
		return eDashboard;
	}

	public Dashboard (File fDashboard) {
		dashboardDir = fDashboard;
		PrivilegeAttribFile dashboardAttrib = new PrivilegeAttribFile(dashboardDir+".atr");
		name = dashboardAttrib.getName(true, 4);
		//System.out.println("\tDashboard: " + sDashboardName);

		traversePages();
		getPageAttributes("/dashboard/@appObjectID");
		permissions = new Vector <Permission> ();
		permissions = (SharedObject.getPrivileges(fDashboard));
	}
}
