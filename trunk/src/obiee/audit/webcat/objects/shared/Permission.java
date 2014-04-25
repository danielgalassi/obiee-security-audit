package obiee.audit.webcat.objects.shared;

import obiee.audit.webcat.engine.WebCatalog;

import org.w3c.dom.Element;


public class Permission {

	private String	sRole;
	private int		iPermValue;
	private String	sPermList;

	public Element serialize() {
		Element ePermission = (WebCatalog.docWebcat).createElement("Permission");
		ePermission.setAttribute("Role", sRole);
		ePermission.setAttribute("Value", iPermValue+"");
		ePermission.setAttribute("Description", sPermList);
		return ePermission;
	}

	public void list() {
		System.out.println(sRole + ": " + iPermValue + "\t(" + sPermList + ")");
	}

	public Permission(String rol, int val, String lst) {
		sRole = rol;
		iPermValue = val;
		sPermList = lst;
	}
}
