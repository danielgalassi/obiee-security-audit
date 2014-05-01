package obiee.audit.webcat.objects.shared;

import obiee.audit.webcat.core.WebCatalog;

import org.w3c.dom.Element;


public class Permission {

	private String         applicationRole;
	private int              weighingValue;
	/** a list of privileges granted to this role */
	private String       grantedPrivileges;

	public Element serialize() {
		Element permission = (WebCatalog.docWebcat).createElement("Permission");
		permission.setAttribute("Role", applicationRole);
		permission.setAttribute("Value", weighingValue+"");
		permission.setAttribute("Description", grantedPrivileges);
		return permission;
	}

	/**
	 * Constructor
	 * @param applicationRole the name of the application role the privilege was granted
	 * @param weighingValue the numerical representation of the granted privileges
	 * @param grantedPrivileges the verbose list of privileges granted to the application role
	 */
	public Permission(String applicationRole, int weighingValue, String grantedPrivileges) {
		this.applicationRole	= applicationRole;
		this.weighingValue		= weighingValue;
		this.grantedPrivileges	= grantedPrivileges;
	}
}
