package obiee.audit.webcat.objects.shared;

import java.io.File;
import java.util.Vector;

import obiee.audit.webcat.core.PrivilegeAttribFile;
import obiee.audit.webcat.core.SharedObject;
import obiee.audit.webcat.core.WebCatalog;

import org.w3c.dom.Element;


public class Report {

//	private static final Logger logger = LogManager.getLogger(Report.class.getName());

	private String	name = "";
	private String	catalogPath;
	private String	owner = "";
	private String	ownerType = "Application Role";
	private boolean ownerIsRole;
	private boolean ownerIsUser;
	private File	reportFile;
	private Vector <Permission> permissions;

	public Element serialize() {
		Element eReport = (WebCatalog.docWebcat).createElement("Report");
		eReport.setAttribute("Name", name);
		eReport.setAttribute("FullUnscrambledName", getFullUnscrambledName());
		eReport.setAttribute("Owner", owner);
		eReport.setAttribute("OwnerType", ownerType);
		eReport.setAttribute("Path", reportFile+"");

		Element ePermissionList = (WebCatalog.docWebcat).createElement("PermissionList");

		for (Permission permission : permissions) {
			ePermissionList.appendChild(permission.serialize());
		}

		eReport.appendChild(ePermissionList);

		return eReport;
	}

	public String getFullUnscrambledName() {
		return (catalogPath + "/" + name);
	}

//	public String getName() {
//		return name;
//	}

//	public void listPrivileges() {
//		for (Permission permission : permissions) {
//			permission.list();
//		}
//	}

	private void setOwner() {
		owner = SharedObject.getOwner(reportFile);
		ownerIsUser = WebCatalog.allUsers.containsKey(owner);
		ownerIsRole = WebCatalog.appRoles.contains(owner);

		if (ownerIsUser) {
			ownerType = "User";
			owner = WebCatalog.allUsers.get(owner);
		}
		if (!ownerIsUser && !ownerIsRole) {
			ownerType = "Not Found";
			owner = "Not Found (" + owner + ")";
		}
	}

	public Report(String path, File s) {
		if (s.canRead()) {
			catalogPath = path;
			reportFile = s;
			PrivilegeAttribFile reportAttrib = new PrivilegeAttribFile(reportFile+".atr");

			name = reportAttrib.getName(false, 4);
			setOwner();

			permissions = (SharedObject.getPrivileges(reportFile));
		}
	}
}
