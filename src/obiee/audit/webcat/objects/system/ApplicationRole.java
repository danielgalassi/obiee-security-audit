package obiee.audit.webcat.objects.system;

import java.io.File;

import obiee.audit.webcat.core.PrivilegeAttribFile;
import obiee.audit.webcat.core.WebCatalog;

import org.w3c.dom.Element;

/***
 * This class represents an OBIEE application role described by its name.
 * @author danielgalassi@gmailc.om
 *
 */
public class ApplicationRole {

	private String name;

	/**
	 * Creates an XML representation of the application role
	 * @return the XML-encoded representation of the role
	 */
	public Element serialize() {
		Element applicationRole = (WebCatalog.docWebcat).createElement("ApplicationRole");
		applicationRole.setAttribute("ApplicationRoleName", name);
		return applicationRole;
	}

	/**
	 * Getter method
	 * @return the name of the application role
	 */
	public String getName() {
		return name;
	}

	/**
	 * Constructor
	 * @param appRole OBIEE role (binary) file
	 */
	public ApplicationRole(File appRole) {
		PrivilegeAttribFile roleAttribute = new PrivilegeAttribFile(appRole.toString());
		name = roleAttribute.getName(false,3);
	}
}
