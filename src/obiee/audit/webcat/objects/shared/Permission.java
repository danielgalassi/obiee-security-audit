package obiee.audit.webcat.objects.shared;

import obiee.audit.webcat.engine.WebCatalog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;


public class Permission {

	private final static Logger logger = LogManager.getLogger(Permission.class.getName());

	private String	applicationRole;
	private int		weighingValue;
	/** a list of privileges granted to this role */
	private String	grantedPrivileges;

	public Element serialize() {
		Element ePermission = (WebCatalog.docWebcat).createElement("Permission");
		ePermission.setAttribute("Role", applicationRole);
		ePermission.setAttribute("Value", weighingValue+"");
		ePermission.setAttribute("Description", grantedPrivileges);
		return ePermission;
	}

	public void list() {
		logger.info("{} : {} + \t({})", applicationRole, weighingValue, grantedPrivileges);
	}

	public Permission(String applicationRole, int weighingValue, String grantedPrivileges) {
		this.applicationRole	= applicationRole;
		this.weighingValue		= weighingValue;
		this.grantedPrivileges	= grantedPrivileges;
	}
}