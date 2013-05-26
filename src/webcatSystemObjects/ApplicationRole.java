package webcatSystemObjects;

import java.io.File;

import org.w3c.dom.Element;

import utils.PrivilegeAttribFile;
import webcatAudit.WebCatalog;

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
