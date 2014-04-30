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
	private String	path;
	private String	owner = "";
	private String	ownerType = "Application Role";
	private boolean ownerIsRole;
	private boolean ownerIsUser;
	private File	file;
	private Vector <Permission> permissions;

	public Element serialize() {
		Element report = (WebCatalog.docWebcat).createElement("Report");
		report.setAttribute("Name", name);
		report.setAttribute("FullUnscrambledName", getFullUnscrambledName());
		report.setAttribute("Owner", owner);
		report.setAttribute("OwnerType", ownerType);
		report.setAttribute("Path", file+"");

		Element permissionList = (WebCatalog.docWebcat).createElement("PermissionList");

		for (Permission permission : permissions) {
			permissionList.appendChild(permission.serialize());
		}

		report.appendChild(permissionList);

		return report;
	}

	public String getFullUnscrambledName() {
		return (path + "/" + name);
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
		owner = SharedObject.getOwner(file);
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

	public Report(String path, File file) {
		if (file.canRead()) {
			this.path = path;
			this.file = file;

			PrivilegeAttribFile reportAttrib = new PrivilegeAttribFile(file+".atr");
			name = reportAttrib.getName(false, 4);
			setOwner();

			permissions = (SharedObject.getPrivileges(file));
		}
	}
}
