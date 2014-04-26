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


public class DashboardPage {

	private static final Logger logger = LogManager.getLogger(DashboardPage.class.getName());

	private File fPage = null;
	private boolean isHidden = false;
	private String name = "";
	private Vector <String> reportPaths = new Vector <String> ();
	private Vector <Permission> permissions;

	private void findReports() {
		if (!SharedObject.isPage(fPage)) {
			return;
		}

		NodeList nTag = null;

		if (fPage.canRead()) {
			Document layoutDOM = XMLUtils.loadDocument(fPage);
			XPath xPath = XPathFactory.newInstance().newXPath();

			try {
				nTag = (NodeList) xPath.evaluate("/dashboardPage//reportRef/@path", layoutDOM.getDocumentElement(), XPathConstants.NODESET);

				if (nTag == null) {
					return;
				}

				//lists each report published on that dashboard page
				for (int i=0; i<nTag.getLength(); i++) {
					reportPaths.add(nTag.item(i).getNodeValue());
				}

			} catch (XPathExpressionException e) {
				logger.error("{} thrown while attempting to find reports featured on a dashboard page", e.getClass().getCanonicalName());
			}
		}
	}

	public Element serialize() {
		Element eDashboardPage = (WebCatalog.docWebcat).createElement("DashboardPage");
		eDashboardPage.setAttribute("DashboardPageName", name);
		eDashboardPage.setAttribute("isHidden", isHidden+"");

		Element eReportList = (WebCatalog.docWebcat).createElement("ReportList");

		for (String s : reportPaths) {
			Element eReport = null;
			if ((WebCatalog.allReports).containsKey(StringEscapeUtils.unescapeJava(s.replace("–", "---")))) {
				eReport = (WebCatalog.allReports).get(StringEscapeUtils.unescapeJava(s.replace("–", "---"))).serialize();
				eReportList.appendChild(eReport);
			}
		}

		Element ePermissionList = (WebCatalog.docWebcat).createElement("PermissionList");

		for (Permission p : permissions)
			ePermissionList.appendChild(p.serialize());

		eDashboardPage.appendChild(ePermissionList);

		eDashboardPage.appendChild(eReportList);
		return eDashboardPage;
	}

	public boolean isHidden() {
		return isHidden;
	}

	private void getPageAttributes(String tag) {
		File fDashLayout = new File(fPage.getParent()+"\\dashboard+layout");

		if (fDashLayout.canRead()) {
			Document layoutDOM = XMLUtils.loadDocument(fDashLayout);
			XPath xPath = XPathFactory.newInstance().newXPath();

			try {
				Node nTag = (Node)xPath.evaluate(tag, layoutDOM.getDocumentElement(), XPathConstants.NODE);
				if (nTag != null) {
					isHidden = (new Boolean(nTag.getNodeValue())).booleanValue();
				}
			} catch (XPathExpressionException e) {
				logger.error("{} thrown while attempting to retrieve Dashboard Page attributes", e.getClass().getCanonicalName());
			}
		}
	}

	public DashboardPage(File file) {
		fPage = file;
		PrivilegeAttribFile pageAttrib = new PrivilegeAttribFile(file+".atr");
		name = pageAttrib.getName(true, 4);
		getPageAttributes("/dashboard/dashboardPageRef[@path='"+name+"']/@hidden");
		findReports();
		permissions = new Vector <Permission> ();
		permissions = (SharedObject.getPrivileges(fPage));
	}
}
