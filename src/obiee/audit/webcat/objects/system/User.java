package obiee.audit.webcat.objects.system;

import java.io.File;

import obiee.audit.webcat.engine.WebCatalog;
import obiee.audit.webcat.utils.PrivilegeAttribFile;

import org.w3c.dom.Element;


public class User {
	
	private String sUserName;
	private String sUserID;

	public Element serialize() {
		Element eUser = (WebCatalog.docWebcat).createElement("User");
		eUser.setAttribute("UserName", sUserName);
		eUser.setAttribute("UserID", sUserID);
		return eUser;
	}

	public String getID() {
		return sUserID;
	}

	public String getName() {
		return sUserName;
	}

	public User(File fUser) {
		PrivilegeAttribFile sNameAttrib = new PrivilegeAttribFile(fUser.toString()+".atr");
		sUserName = sNameAttrib.getName(false,4);
		PrivilegeAttribFile sIDAttrib = new PrivilegeAttribFile(fUser.toString());
		sUserID = sIDAttrib.getName(false,3);
	}
}
