package webcatSharedObjects;

import java.io.File;
import java.util.ListIterator;
import java.util.Vector;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utils.PrivilegeAttribFile;
import utils.SharedObject;
import utils.XMLUtils;
import webcatAudit.WebCatalog;

public class DashboardPage {

	private File fPage = null;
	private boolean isHidden = false;
	private String sPageName = "";
	private Vector <String> vsReportPaths = new Vector <String> ();

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
				for (int i=0; i<nTag.getLength(); i++) {
					System.out.println("\t\t\tReport123: " + nTag.item(i).getNodeValue() + "\t" + StringEscapeUtils.unescapeJava(nTag.item(i).getNodeValue().replace("–", "-")));
					vsReportPaths.add(nTag.item(i).getNodeValue());
				}

			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}
	}

	public Element serialize() {
		Element eDashboardPage = (WebCatalog.docWebcat).createElement("DashboardPage");
		eDashboardPage.setAttribute("DashboardPageName", sPageName);
		eDashboardPage.setAttribute("isHidden", isHidden+"");

		Element eReportList = (WebCatalog.docWebcat).createElement("ReportList");
		ListIterator <String> li = vsReportPaths.listIterator();
		while (li.hasNext()) {
			String s = li.next();
System.out.println("12313123" + StringEscapeUtils.unescapeJava(s.replace("–", "-")));
			Element eReport = (WebCatalog.hmAllReports).get(StringEscapeUtils.unescapeJava(s.replace("–", "-"))).serialize();
			eReportList.appendChild(eReport);
		}

		eDashboardPage.appendChild(eReportList);
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
		sPageName = pageAttrib.getName(true);
		getPageAttributes("/dashboard/dashboardPageRef[@path='"+sPageName+"']/@hidden");
		System.out.println("\t\tPage: " + sPageName);
		findReports();
	}
}
