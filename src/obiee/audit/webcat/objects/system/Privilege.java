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

	private PrivilegeAttribFile      privilegeAttribute;
	private File                           privilegeDir;
	private String                                 name = "";
	private Vector <String>                     granted = new Vector <String> ();
	private Vector <String>                      denied = new Vector <String> ();
	private static final String[]             ootbRoles = {"BIConsumer", "BIAuthor", "BIAdministrator"};
	
	/**
	 * Provides a list of roles that have been granted access to the privilege.
	 * @return a set of roles 
	 */
	public Vector <String> getRolesGrantedAccess () {
		return granted;
	}

	/**
	 * Provides a list of roles that have been denied access to the privilege.
	 * @return a set of roles
	 */
	public Vector <String> getRolesDeniedAccess () {
		return denied;
	}

	/***
	 * Parse files to find all groups / roles that have been granted / denied access to a feature.
	 * The way roles are read is split in 2 big areas: 1) find the number of roles to read and 2) retrieve N groups
	 */
	private void readRoles () {
		FileInputStream file_input = null;
		DataInputStream data_in    = null;

		byte b_data = 0;
		int iRead;
		int iGroupLength;
		int groupQuantity;
		String temporaryGroupName;

		try {
			file_input = new FileInputStream(privilegeDir.toString());
			data_in = new DataInputStream (file_input);

			//ignoring first two bytes
			for (int i = 0; i<2; i++) {
				data_in.read();
			}

			//reading the number of groups granted access in this file
			groupQuantity = data_in.read();

			for (int n=0; n<groupQuantity; n++) {

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
	 * Builds an XML representation of the object
	 * @return XML-encoded Privilege representation 
	 */
	public Element serialize () {
		Element role;
		Node content;

		Element privilege = (WebCatalog.docWebcat).createElement("Privilege");
		Element roleList = (WebCatalog.docWebcat).createElement("RoleList");

		for (String roleName : granted) {
			content = (WebCatalog.docWebcat).createTextNode(roleName);
			role = (WebCatalog.docWebcat).createElement("Role");
			role.appendChild(content);
			role.setAttribute("access", "Granted");
			role.setAttribute("isOOTBRole", isOOTBRole(roleName)+"");
			roleList.appendChild(role);
		}
		
		for (String roleName : denied) {
			content = (WebCatalog.docWebcat).createTextNode(roleName);
			role = (WebCatalog.docWebcat).createElement("Role");
			role.appendChild(content);
			role.setAttribute("access", "Granted");
			role.setAttribute("isOOTBRole", isOOTBRole(roleName)+"");
			roleList.appendChild(role);
		}

		privilege.setAttribute("PrivilegeName", name);
		privilege.appendChild(roleList);

		return privilege;
	}

	/***
	 * Constructor
	 * @param privilegeFile a binary file representing an OBIEE privilege 
	 */
	public Privilege (File privilegeFile, String group) {
		if (privilegeFile.canRead()) {
			privilegeAttribute = new PrivilegeAttribFile(privilegeFile.toString());

			name = privilegeAttribute.getName(true, 4);

			privilegeDir = new File(privilegeAttribute.getAttribDir());
			if (!privilegeDir.canRead()) {
				privilegeDir = null;
			}
			else {
				readRoles();
			}
		}
	}
}
