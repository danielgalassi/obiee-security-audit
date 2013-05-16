package webcatSharedObjects;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utils.PrivilegeAttribFile;
import webcatAudit.WebCatalog;
import xmlutils.XMLUtils;

public class DashboardPage {

	private File fPage = null;
	private boolean isHidden = false;
	private String sPageName = "";

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
		Document dashLayoutDOM = null;
		NamedNodeMap attribs = null;
		Node dashboardTag = null;
		NodeList dashChildNodes = null;

		if (fDashLayout.canRead()) {
			dashLayoutDOM = XMLUtils.File2Document(fDashLayout);
			dashboardTag = dashLayoutDOM.getFirstChild();
			if (dashboardTag.getNodeName().equals("sawd:dashboard")) {
				if (dashboardTag.hasChildNodes()) {
					dashChildNodes = dashboardTag.getChildNodes();
					for (int x=0; x<dashChildNodes.getLength(); x++) {
						attribs = dashChildNodes.item(x).getAttributes();
						if (attribs != null) {
							isHidden = (new Boolean(attribs.getNamedItem(tag).getNodeValue())).booleanValue();
						}
					}
				}
			}
		}
	}

	public DashboardPage(File file) {
		fPage = file;
		PrivilegeAttribFile pageAttrib = new PrivilegeAttribFile(file+".atr");
		sPageName = pageAttrib.getName();
		getPageAttributes("hidden");
		System.out.println("\t\t" + sPageName);
	}

}
