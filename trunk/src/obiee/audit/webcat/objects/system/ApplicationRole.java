package obiee.audit.webcat.objects.system;

import java.io.File;

import obiee.audit.webcat.core.PrivilegeAttribFile;
import obiee.audit.webcat.core.WebCatalog;

import org.w3c.dom.Element;


public class ApplicationRole {

	private String name;

	public Element serialize() {
		Element eRole = (WebCatalog.docWebcat).createElement("ApplicationRole");
		eRole.setAttribute("ApplicationRoleName", name);
		return eRole;
	}

	public String getName() {
		return name;
	}

	public ApplicationRole(File appRole) {
		PrivilegeAttribFile roleAttribute = new PrivilegeAttribFile(appRole.toString());
		name = roleAttribute.getName(false,3);
	}
}
