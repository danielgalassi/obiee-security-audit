package obiee.audit.webcat.objects.shared;

import obiee.audit.webcat.engine.WebCatalog;

import org.w3c.dom.Element;


public class Permission {

	private String	role;
	private int		iPermValue;
	private String	roleDescription;

	public Element serialize() {
		Element ePermission = (WebCatalog.docWebcat).createElement("Permission");
		ePermission.setAttribute("Role", role);
		ePermission.setAttribute("Value", iPermValue+"");
		ePermission.setAttribute("Description", roleDescription);
		return ePermission;
	}

	public void list() {
		System.out.println(role + ": " + iPermValue + "\t(" + roleDescription + ")");
	}

	public Permission(String rol, int val, String lst) {
		role = rol;
		iPermValue = val;
		roleDescription = lst;
	}
}
