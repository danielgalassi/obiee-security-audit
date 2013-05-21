package webcatSharedObjects;

import java.io.File;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import utils.PrivilegeAttribFile;
import utils.SharedObject;
import webcatAudit.WebCatalog;
import xmlutils.XMLUtils;

public class DashboardPage {

	private File fPage = null;
	private boolean isHidden = false;
	private String sPageName = "";

	private Node findReports() {
		
		if (!SharedObject.isPage(fPage))
			return null;

		Node nTag = null;
		System.out.println("My name is " + sPageName + "\t" + fPage);

		if (fPage.canRead()) {
			Document layoutDOM = XMLUtils.File2Document(fPage);
			XPath xPath = XPathFactory.newInstance().newXPath();

			try {
				nTag = (Node) xPath.evaluate("/dashboardPage//reportRef/@path",
						layoutDOM.getDocumentElement(),
						XPathConstants.NODE);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}

		}
		return nTag;
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
		System.out.println(findReports());
	}
}
