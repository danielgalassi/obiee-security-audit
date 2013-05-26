package webcatSharedObjects;

import java.io.File;
import java.util.ListIterator;
import java.util.Vector;

import org.w3c.dom.Element;

import utils.PrivilegeAttribFile;
import utils.SharedObject;
import webcatAudit.WebCatalog;

public class Report {

	private String	sReportName = "";
	private String	sCatalogPath;
	private String	sOwner = "";
	private File	fReport;
	private Vector <Permission> vPerms;

	public Element serialize() {
		Element eReport = (WebCatalog.docWebcat).createElement("Report");
		eReport.setAttribute("Name", sReportName);
		eReport.setAttribute("FullUnscrambledName", getFullUnscrambledName());
		eReport.setAttribute("Owner", sOwner);
		eReport.setAttribute("Path", fReport+"");

		Element ePermissionList = (WebCatalog.docWebcat).createElement("PermissionList");
		ListIterator <Permission> li = vPerms.listIterator();

		while (li.hasNext())
			ePermissionList.appendChild(li.next().serialize());

		eReport.appendChild(ePermissionList);

		return eReport;
	}

	public String getFullUnscrambledName() {
		return (sCatalogPath + "/" + sReportName);
	}

	public String getName() {
		return sReportName;
	}

	public void listPrivileges() {
		ListIterator <Permission> li = vPerms.listIterator();
		while (li.hasNext())
			(li.next()).list();
	}

	public Report(String uPath, File s) {
		if (s.canRead()) {
			sCatalogPath = uPath;
			fReport = s;
			PrivilegeAttribFile reportAttrib = new PrivilegeAttribFile(fReport+".atr");

			sReportName = reportAttrib.getName(false,4);
			sOwner = (SharedObject.getOwner(fReport));
			if (WebCatalog.hmAllUsers.containsKey(sOwner))
				sOwner = WebCatalog.hmAllUsers.get(sOwner);
			else
				sOwner = "User Not Found";
			vPerms = (SharedObject.getPrivileges(fReport));
		}
	}
}
