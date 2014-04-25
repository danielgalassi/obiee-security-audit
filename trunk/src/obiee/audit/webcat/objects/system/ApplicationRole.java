package obiee.audit.webcat.objects.system;

import java.io.File;

import obiee.audit.webcat.engine.WebCatalog;
import obiee.audit.webcat.utils.PrivilegeAttribFile;

import org.w3c.dom.Element;


public class ApplicationRole {

	private String sAppRoleName;

	public Element serialize() {
		Element eRole = (WebCatalog.docWebcat).createElement("ApplicationRole");
		eRole.setAttribute("ApplicationRoleName", sAppRoleName);
		return eRole;
	}

	public String getName() {
		return sAppRoleName;
	}

	public ApplicationRole(File fAppRole) {
		PrivilegeAttribFile roleAttrib = new PrivilegeAttribFile(fAppRole.toString());
		sAppRoleName = roleAttrib.getName(false,3);
	}
}
