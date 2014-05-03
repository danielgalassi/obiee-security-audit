package obiee.audit.webcat.objects.system;

import java.io.File;

import obiee.audit.webcat.core.PrivilegeAttribFile;
import obiee.audit.webcat.core.WebCatalog;

import org.w3c.dom.Element;

/**
 * This class represents an OBIEE user account. Each user is identified by a name and an ID.
 * @author danielgalassi@gmail.com
 *
 */
public class User implements OBIAccount {

	private String   id;
	private String name;

	public Element serialize() {
		Element user = (WebCatalog.docWebcat).createElement("User");
		user.setAttribute("UserName", name);
		user.setAttribute("UserID", id);
		return user;
	}

	public String getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public User(File userFile) {
		PrivilegeAttribFile nameAttribute = new PrivilegeAttribFile(userFile.toString() + ".atr");
		name = nameAttribute.getName(false, 4);
		PrivilegeAttribFile idAttribute = new PrivilegeAttribFile(userFile.toString());
		id = idAttribute.getName(false, 3);
	}
}
