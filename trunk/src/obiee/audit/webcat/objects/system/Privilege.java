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
 * A privilege is a specific task OBIEE users can perform on Dashboards, Answers and other applications.
 * OBIEE roles are granted or denied the ability to perform these tasks.
 * @author danielgalassi@gmail.com
 *
 */
public class Privilege {

	private static final Logger logger = LogManager.getLogger(Privilege.class.getName());

	private PrivilegeAttribFile      privilegeAttribute;
	private File                           privilegeDir;
	private String                                 name = "";
	/** application roles that have been granted the ability to perform the privilege */
	private Vector <String>                     granted = new Vector <String> ();
	/** application roles that have been denied the ability to perform the privilege */
	private Vector <String>                      denied = new Vector <String> ();
	/** out-of-the-box roles shipped with OBIEE */
	private static final String[]             ootbRoles = {"BIConsumer", "BIAuthor", "BIAdministrator"};

	/**
	 * Provides a list of roles that have been granted access to the privilege.
	 * @return a set of roles 
	 */
	public Vector<String> getRolesGrantedAccess () {
		return granted;
	}

	/**
	 * Provides a list of roles that have been denied access to the privilege.
	 * @return a set of roles
	 */
	public Vector<String> getRolesDeniedAccess () {
		return denied;
	}

	/***
	 * Parses files to find all groups / roles that have been granted / denied access to a feature.
	 * The way roles are read is split in 2 big areas: 1) find the number of roles to read and 2) retrieve N groups
	 */
	private void readRoles () {
		FileInputStream file_input = null;
		DataInputStream data_in    = null;

		byte b_data = 0;
		int groupNameLength;
		int groupCount;
		String temporaryGroupName;

		try {
			file_input = new FileInputStream(privilegeDir.toString());
			data_in = new DataInputStream (file_input);

			data_in.skipBytes(2);

			//reading the number of groups granted access in this file
			groupCount = data_in.read();

			for (int n=0; n<groupCount; n++) {

				//skipping bytes till the "size mark" (2) is found
				boolean markReached = false;
				while (!markReached) {
					markReached = (data_in.read() == 2);
				}

				groupNameLength = data_in.read();

				data_in.skipBytes(3);

				temporaryGroupName = "";
				for (int j = 0; j<groupNameLength; j++) {
					b_data = data_in.readByte();
					char c = (char)b_data;
					if (b_data < 0)
						c = '-';
					temporaryGroupName = temporaryGroupName + c;
				}

				switch (data_in.read()) {
				case 0 :
					denied.add(temporaryGroupName);
					break;
				default:
					granted.add(temporaryGroupName);
					break;
				}
			}
		} catch (IOException e) {
			logger.error("{} thrown while reading privileges", e.getClass().getCanonicalName());
		}
	}

	/**
	 * Evaluates whether a role is out-of-the-box or not.
	 * @param role the name of an OBIEE application role
	 * @return true when evaluating an OBIE 11g OOTB role
	 */
	private boolean isOOTBRole (String role) {
		boolean isOOTB = false;

		for (String ootbRole : ootbRoles) {
			if (ootbRole.equals(role)) {
				isOOTB = true;
				break;
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
			role.setAttribute("access", "Denied");
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
