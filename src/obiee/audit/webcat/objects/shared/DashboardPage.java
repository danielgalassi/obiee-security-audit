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
import obiee.audit.webcat.utils.XMLUtils;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * OBIEE dashboard pages feature presentation attributes such as reports, images, etc.
 * @author danielgalassi@gmail.com
 *
 */
public class DashboardPage extends SharedObject {

	private static final Logger logger = LogManager.getLogger(DashboardPage.class.getName());

	private File                         page = null;
	private boolean                  isHidden = false;
	private String                       name = "";
	private Vector <String>       reportPaths = new Vector <String> ();
	private Vector <Permission>   permissions;

	/**
	 * Examines reports featured on a dashboard page.
	 */
	private void findReports() {
		if (!isPage(page)) {
			return;
		}

		NodeList reportRefs = null;

		if (page.canRead()) {
			Document dashboardLayout = XMLUtils.loadDocument(page);
			XPath xPath = XPathFactory.newInstance().newXPath();

			try {
				reportRefs = (NodeList) xPath.evaluate("/dashboardPage//reportRef/@path", dashboardLayout.getDocumentElement(), XPathConstants.NODESET);

				if (reportRefs == null) {
					return;
				}

				//lists each report published on that dashboard page
				for (int i=0; i<reportRefs.getLength(); i++) {
					reportPaths.add(reportRefs.item(i).getNodeValue());
				}

			} catch (XPathExpressionException e) {
				logger.error("{} thrown while attempting to find reports featured on a dashboard page", e.getClass().getCanonicalName());
			}
		}
	}

	public Element serialize() {
		Element dashboardPage = (WebCatalog.docWebcat).createElement("DashboardPage");
		dashboardPage.setAttribute("DashboardPageName", name);
		dashboardPage.setAttribute("isHidden", isHidden+"");

		Element reportList = (WebCatalog.docWebcat).createElement("ReportList");

		for (String s : reportPaths) {
			Report report = (WebCatalog.reports).get(StringEscapeUtils.unescapeJava(s.replace("�", "---")));
			if (report != null) {
				reportList.appendChild(report.serialize());
			}
		}

		Element permissionList = (WebCatalog.docWebcat).createElement("PermissionList");

		for (Permission permission : permissions) {
			permissionList.appendChild(permission.serialize());
		}

		dashboardPage.appendChild(permissionList);
		dashboardPage.appendChild(reportList);

		return dashboardPage;
	}

	private void getPageAttributes(String tag) {
		File pageLayout = new File(page.getParent()+"\\dashboard+layout");

		if (pageLayout.canRead()) {
			Document layoutDOM = XMLUtils.loadDocument(pageLayout);
			XPath xPath = XPathFactory.newInstance().newXPath();

			try {
				Node node = (Node)xPath.evaluate(tag, layoutDOM.getDocumentElement(), XPathConstants.NODE);
				if (node != null) {
					isHidden = (new Boolean(node.getNodeValue())).booleanValue();
				}
			} catch (XPathExpressionException e) {
				logger.error("{} thrown while attempting to retrieve Dashboard Page attributes", e.getClass().getCanonicalName());
			}
		}
	}

	/**
	 * Constructor
	 * @param page a file representing an OBIEE dashboard page.
	 */
	public DashboardPage(File page) {
		this.page = page;
		PrivilegeAttribFile pageAttrib = new PrivilegeAttribFile(page+".atr");
		name = pageAttrib.getName(true, 4);
		getPageAttributes("/dashboard/dashboardPageRef[@path='"+name+"']/@hidden");
		findReports();
		permissions = getPrivileges(page);
	}
}
