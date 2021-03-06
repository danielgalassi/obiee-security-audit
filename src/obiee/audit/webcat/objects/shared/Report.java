package obiee.audit.webcat.objects.shared;

import java.io.File;
import java.util.Vector;

import obiee.audit.webcat.core.PrivilegeAttribFile;
import obiee.audit.webcat.core.SharedObject;
import obiee.audit.webcat.core.WebCatalog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;


public class Report extends SharedObject {

	private static final Logger logger = LogManager.getLogger(Report.class.getName());

	/** the unscrambled name (not the title view) of the report as read by users when using OBIEE */
	private String                     name = "";
	/** the application role or user account set as the owner of the analysis */
	private String                    owner = "";
	private String                ownerType = "Application Role";
	/** location of this analysis within the presentation catalogue */
	private String                     path;
	private boolean             ownerIsRole;
	private boolean             ownerIsUser;
	/** file containing the XML representation of the analysis */
	private File	                   file;
	/** file containing the security and system details of the analysis */
	PrivilegeAttribFile           attribute;
	/** the set of permissions granted to application roles and user accounts on the analysis */
	private Vector <Permission> permissions;

	/**
	 * Creates a representation of the analysis including all retrieved fields and derived details.
	 * @return an XML representation of the analysis
	 */
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

	/**
	 * Getter method
	 * @return the location and unscrambled name of the analysis
	 */
	public String getFullUnscrambledName() {
		return (path + "/" + name);
	}

	/***
	 * Retrieves the name for this analysis
	 */
	private void setName() {
		if (!attribute.canRead()) {
			return;
		}
		this.name = attribute.getName(false, 4);
	}

	/***
	 * Retrieves the application role or user set as the owner of this analysis
	 */
	private void setOwner() {
		if (!attribute.canRead()) {
			return;
		}

		owner = attribute.getOwner();
		ownerIsUser = WebCatalog.users.containsKey(owner);
		ownerIsRole = WebCatalog.appRoles.contains(owner);

		if (ownerIsUser) {
			ownerType = "User";
			owner = WebCatalog.users.get(owner);
		}

		if (!ownerIsUser && !ownerIsRole) {
			logger.warn("The owner of {} ({}) could not be matched to a valid user or role", name, owner);
			ownerType = "Not Found";
			owner = "Not Found (" + owner + ")";
		}
	}

	/**
	 * Constructor
	 * @param path location of the report under shared folder
	 * @param file XML file representing an OBIEE analysis
	 */
	public Report(String path, File file) {
		if (file.canRead()) {
			this.path = path;
			this.file = file;

			attribute = new PrivilegeAttribFile(file+".atr");
			setName();
			setOwner();

			permissions = getPrivileges(file);
		}
	}

}
