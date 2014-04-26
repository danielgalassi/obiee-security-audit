package obiee.audit.webcat.objects.system;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import obiee.audit.webcat.core.PrivilegeAttribFile;
import obiee.audit.webcat.core.WebCatalog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * 
 * @author danielgalassi@gmail.com
 *
 */
public class Privilege {

	private static final Logger logger = LogManager.getLogger(Privilege.class.getName());

	private PrivilegeAttribFile privilegeAttribute;
	private File privilegeDir;
	private String privilegeName = "";
	private Vector <String> granted = new Vector <String> ();
	private Vector <String> denied = new Vector <String> ();
	private static final String[] ootbRoles = {"BIConsumer", "BIAuthor", "BIAdministrator"};
	
	/**
	 * Provides a list of roles that have been granted access to the privilege.
	 * @return List of roles as stored in 
	 */
	public Vector <String> getRolesGrantedAccess () {
		return granted;
	}

	public Vector <String> getRolesDeniedAccess () {
		return denied;
	}

	/***
	 * Parse files to find all groups / roles that have been granted / denied access to a feature.
	 */
	private void readPrivileges () {
		FileInputStream file_input = null;
		DataInputStream data_in    = null;

		byte b_data = 0;
		int iRead;
		int iGroupLength;
		int nGroups;
		String temporaryGroupName;

		try {
			file_input = new FileInputStream(privilegeDir.toString());
			data_in = new DataInputStream (file_input);

			//ignoring first two bytes
			for (int i = 0; i<2; i++) {
				data_in.read();
			}

			//reading the number of groups granted access in this file
			nGroups = data_in.read();

			for (int n=0; n<nGroups; n++) {

				//skipping bytes till the "size mark" (2) is found
				iRead = data_in.read();
				while (iRead != 2) {
					iRead = data_in.read();
				}

				iGroupLength = data_in.read();

				//ignoring next three bytes
				for (int i=0; i<3; i++) {
					data_in.read();
				}

				temporaryGroupName = "";
				for (int j = 0; j<iGroupLength; j++) {
					b_data = data_in.readByte();
					char c = (char)b_data;
					if (b_data < 0)
						c = '-';
					temporaryGroupName = temporaryGroupName + c;
				}
				if (data_in.read() == 0) {
					denied.add (temporaryGroupName);
				}
				else {
					granted.add (temporaryGroupName);
				}
			}
		} catch (IOException e) {
			logger.error("{} thrown while reading privileges", e.getClass().getCanonicalName());
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getName () {
		return privilegeName;
	}

	/**
	 * Evaluates whether a role is out-of-the-box or not.
	 * @param roleName
	 * @return true when evaluating an OBIE 11g OOTB role.
	 */
	private boolean isOOTBRole (String roleName) {
		boolean isOOTB = false;

		for (String role : ootbRoles) {
			if (role.equals(roleName)) {
				isOOTB = true;
			}
		}

		return isOOTB;
	}
	
	/**
	 * 
	 * @return
	 */
	public Element serialize () {
		Element eRole;
		Node nRole;

		Element ePriv = (WebCatalog.docWebcat).createElement("Privilege");
		Element eRoleList = (WebCatalog.docWebcat).createElement("RoleList");

		for (String s : granted) {
			nRole = (WebCatalog.docWebcat).createTextNode(s);
			eRole = (WebCatalog.docWebcat).createElement("Role");
			eRole.appendChild(nRole);
			eRole.setAttribute("access", "Granted");
			eRole.setAttribute("isOOTBRole", isOOTBRole(s)+"");
			eRoleList.appendChild(eRole);
		}
		
		for (String s : denied) {
			nRole = (WebCatalog.docWebcat).createTextNode(s);
			eRole = (WebCatalog.docWebcat).createElement("Role");
			eRole.appendChild(nRole);
			eRole.setAttribute("access", "Granted");
			eRole.setAttribute("isOOTBRole", isOOTBRole(s)+"");
			eRoleList.appendChild(eRole);
		}

		ePriv.setAttribute("PrivilegeName", privilegeName);
		ePriv.appendChild(eRoleList);

		return ePriv;
	}

	/***
	 * 
	 * @param privilegeFile
	 */
	public Privilege (File privilegeFile, String group) {
		if (privilegeFile.canRead()) {
			privilegeAttribute = new PrivilegeAttribFile(privilegeFile.toString());

			privilegeName = privilegeAttribute.getName(true, 4);

			privilegeDir = new File(privilegeAttribute.getAttribDir());
			if (!privilegeDir.canRead()) {
				privilegeDir = null;
			}
			else {
				readPrivileges();
			}
		}
	}
}
