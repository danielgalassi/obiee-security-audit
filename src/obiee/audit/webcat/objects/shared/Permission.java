package obiee.audit.webcat.objects.shared;

import obiee.audit.webcat.engine.WebCatalog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;


public class Permission {

	private final static Logger logger = LogManager.getLogger(Permission.class.getName());

	private String	role;
	private int		weighingValue;
	private String	roleDescription;

	public Element serialize() {
		Element ePermission = (WebCatalog.docWebcat).createElement("Permission");
		ePermission.setAttribute("Role", role);
		ePermission.setAttribute("Value", weighingValue+"");
		ePermission.setAttribute("Description", roleDescription);
		return ePermission;
	}

	public void list() {
		logger.info("{} : {} + \t({})", role, weighingValue, roleDescription);
	}

	public Permission(String rol, int val, String lst) {
		role = rol;
		weighingValue = val;
		roleDescription = lst;
	}
}
