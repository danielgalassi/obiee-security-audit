package obiee.audit.webcat.objects.system;

import java.io.File;

import obiee.audit.webcat.core.PrivilegeAttribFile;
import obiee.audit.webcat.core.WebCatalog;

import org.w3c.dom.Element;

/***
 * This class represents an application role described by its name.
 * OBIEE installations organise security, policies and group of users around WebLogic application roles.
 * @author danielgalassi@gmail.com
 *
 */
public class ApplicationRole {

	/** the name of the WLS / OBIEE application role */
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
		name = roleAttribute.getName(false, 3);
	}
}
