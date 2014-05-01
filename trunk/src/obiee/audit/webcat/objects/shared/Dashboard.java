package obiee.audit.webcat.objects.shared;

import java.io.File;
import java.util.Vector;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import obiee.audit.webcat.core.PrivilegeAttribFile;
import obiee.audit.webcat.core.SharedObject;
import obiee.audit.webcat.core.WebCatalog;
import obiee.audit.webcat.filters.PageFilter;
import obiee.audit.webcat.utils.XMLUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class Dashboard {

	private static final Logger logger = LogManager.getLogger(Dashboard.class.getName());

	private File                     dashboardDir = null;
	private boolean                        isOOTB = false;
	private String                           name = "";
	private Vector <DashboardPage>          pages;
	private Vector <Permission>       permissions;

	private void getPageAttributes(String tag) {
		File fDashLayout = new File(dashboardDir+"\\dashboard+layout");
		if (fDashLayout.canRead()) {
			Document dashLayoutDOM = XMLUtils.loadDocument(fDashLayout);
			XPath xPath = XPathFactory.newInstance().newXPath();

			try {
				Node node = (Node) xPath.evaluate(tag, dashLayoutDOM.getDocumentElement(), XPathConstants.NODE);
				if (node != null) {
					isOOTB = true;
				}
			} catch (XPathExpressionException e) {
				logger.error("{} thrown while attempting to get Dashboard Page attributes", e.getClass().getCanonicalName());
			}
		}
	}

	/**
	 * This method catalogues dashboard pages
	 */
	private void traversePages() {
		pages = new Vector <DashboardPage> ();

		for (File page : dashboardDir.listFiles(new PageFilter())) {
			pages.add(new DashboardPage(page));
		}

	}

	public Element serialize() {
		Element dashboardPageList = (WebCatalog.docWebcat).createElement("DashboardPageList");
		Element dashboard = (WebCatalog.docWebcat).createElement("Dashboard");
		dashboard.setAttribute("DashboardName", name);
		dashboard.setAttribute("isOOTB", isOOTB+"");

		for (DashboardPage page : pages) {
			dashboardPageList.appendChild(page.serialize());
		}

		Element permissionList = (WebCatalog.docWebcat).createElement("PermissionList");

		for (Permission permission : permissions) {
			permissionList.appendChild(permission.serialize());
		}

		dashboard.appendChild(permissionList);

		dashboard.appendChild(dashboardPageList);
		return dashboard;
	}

	/**
	 * Constructor
	 * @param dashboard a file representing an OBIEE dashboard
	 */
	public Dashboard (File dashboard) {
		dashboardDir = dashboard;
		PrivilegeAttribFile dashboardAttrib = new PrivilegeAttribFile(dashboardDir+".atr");
		name = dashboardAttrib.getName(true, 4);
		logger.trace("Retrieving {} dashboard", name);

		traversePages();
		getPageAttributes("/dashboard/@appObjectID");
		permissions = (SharedObject.getPrivileges(dashboardDir));
	}
}
