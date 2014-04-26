package obiee.audit.webcat.objects.system;

import java.io.File;

import obiee.audit.webcat.core.PrivilegeAttribFile;
import obiee.audit.webcat.core.WebCatalog;

import org.w3c.dom.Element;


public class User {
	
	private String name;
	private String id;

	public Element serialize() {
		Element eUser = (WebCatalog.docWebcat).createElement("User");
		eUser.setAttribute("UserName", name);
		eUser.setAttribute("UserID", id);
		return eUser;
	}

	public String getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public User(File userFile) {
		PrivilegeAttribFile nameAttribute = new PrivilegeAttribFile(userFile.toString()+".atr");
		name = nameAttribute.getName(false, 4);
		PrivilegeAttribFile idAttribute = new PrivilegeAttribFile(userFile.toString());
		id = idAttribute.getName(false, 3);
	}
}
