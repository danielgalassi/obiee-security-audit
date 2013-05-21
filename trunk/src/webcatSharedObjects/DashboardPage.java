package webcatSharedObjects;

import java.io.File;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utils.PrivilegeAttribFile;
import utils.SharedObject;
import webcatAudit.WebCatalog;
import xmlutils.XMLUtils;

public class DashboardPage {

	private File fPage = null;
	private boolean isHidden = false;
	private String sPageName = "";

	private void findReports() {

		if (!SharedObject.isPage(fPage))
			return;

		NodeList nTag = null;

		if (fPage.canRead()) {
			Document layoutDOM = XMLUtils.File2Document(fPage);
			XPath xPath = XPathFactory.newInstance().newXPath();

			try {
				nTag = (NodeList) xPath.evaluate("/dashboardPage//reportRef/@path",
						layoutDOM.getDocumentElement(),
						XPathConstants.NODESET);

				if (nTag == null)
					return;

				//lists each report published on that dashboard page
				for (int i=0; i<nTag.getLength(); i++)
					System.out.println("\t\t\t" + nTag.item(i).getNodeValue());

			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}
	}

	public Element serialize() {
		Element eDashboardPage = (WebCatalog.docWebcat).createElement("DashboardPage");
		eDashboardPage.setAttribute("DashboardPageName", sPageName);
		eDashboardPage.setAttribute("isHidden", isHidden+"");
		return eDashboardPage;
	}

	public boolean isHidden() {
		return isHidden;
	}

	private void getPageAttributes(String tag) {
		File fDashLayout = new File(fPage.getParent()+"\\dashboard+layout");

		if (fDashLayout.canRead()) {
			Document layoutDOM = XMLUtils.File2Document(fDashLayout);
			XPath xPath = XPathFactory.newInstance().newXPath();

			try {
				Node nTag = (Node)xPath.evaluate(tag, 
						layoutDOM.getDocumentElement(),
						XPathConstants.NODE);
				if (nTag != null)
					isHidden = (new Boolean(nTag.getNodeValue())).booleanValue();
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}
	}

	public DashboardPage(File file) {
		fPage = file;
		PrivilegeAttribFile pageAttrib = new PrivilegeAttribFile(file+".atr");
		sPageName = pageAttrib.getName();
		getPageAttributes("/dashboard/dashboardPageRef[@path='"+sPageName+"']/@hidden");
		System.out.println("\t\tPage: " + sPageName);
		if (fPage.getAbsolutePath().contains("support"))
			findReports();
	}
}
