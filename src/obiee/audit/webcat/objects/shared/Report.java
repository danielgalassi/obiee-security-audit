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
		Element report = (WebCatalog.docWebcat).createElement("Report");
		report.setAttribute("Name", name);
		report.setAttribute("FullUnscrambledName", getFullUnscrambledName());
		report.setAttribute("Owner", owner);
		report.setAttribute("OwnerType", ownerType);
		report.setAttribute("Path", reportFile+"");

		Element permissionList = (WebCatalog.docWebcat).createElement("PermissionList");

		for (Permission permission : permissions) {
			permissionList.appendChild(permission.serialize());
		}

		report.appendChild(permissionList);

		return report;
	}

	public String getFullUnscrambledName() {
		return (catalogPath + "/" + name);
	}

	public String getName() {
		return name;
	}

	public void listPrivileges() {
		for (Permission permission : permissions) {
			permission.list();
		}
	}

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
