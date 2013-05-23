package webcatSystemObjects;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import utils.PrivilegeAttribFile;
import webcatAudit.WebCatalog;

/**
 * 
 * @author danielgalassi@gmail.com
 *
 */
public class Privilege {

	private PrivilegeAttribFile privilegeAttrib;
	private File privilegeDir;
	private String sPrivName = "";
	private Vector <String> vsGranted;
	private Vector <String> vsDenied;
	private static final String[] sOOTBRoles = {"BIConsumer",
												"BIAuthor",
												"BIAdministrator"};
	
	/**
	 * Provides a list of roles that have been granted access to the privilege.
	 * @return List of roles as stored in 
	 */
	public Vector <String> getRolesGrantedAccess () {
		return vsGranted;
	}

	public Vector <String> getRolesDeniedAccess () {
		return vsDenied;
	}

	/***
	 * Parse files to find all groups / roles that have been granted / denied access to a feature.
	 */
	private void readPrivileges () {
		FileInputStream file_input = null;
		DataInputStream data_in    = null;

		vsGranted = new Vector <String> ();
		vsDenied  = new Vector <String> ();

		byte	b_data = 0;
		int iRead;
		int iGroupLength;
		int nGroups;
		String sTempGroupName;

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
				while (iRead != 2)
					iRead = data_in.read();

				iGroupLength = data_in.read();

				//ignoring next three bytes
				for (int i=0; i<3; i++)
					data_in.read();

				sTempGroupName = "";
				for (int j = 0; j<iGroupLength; j++) {
					b_data = data_in.readByte();
					char c = (char)b_data;
					if (b_data < 0)
						c = '-';
					sTempGroupName = sTempGroupName + c;
				}
				if (data_in.read() == 0)
					vsDenied.add (sTempGroupName);
				else
					vsGranted.add (sTempGroupName);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getName () {
		return sPrivName;
	}

	/**
	 * Evaluates whether a role is out-of-the-box or not.
	 * @param sRoleName
	 * @return true when evaluating an OBIE 11g OOTB role.
	 */
	private boolean isOOTBRole (String sRoleName) {
		boolean isOOTB = false;
		for (int i=0; i<sOOTBRoles.length; i++)
			if (sOOTBRoles[i].equals(sRoleName))
				isOOTB = true;
		return isOOTB;
	}
	
	/**
	 * 
	 * @return
	 */
	public Element serialize () {
		Element eRole;
		Node nRole;
		String p;

		Element ePriv = (WebCatalog.docWebcat).createElement("Privilege");
		Element eRoleList = (WebCatalog.docWebcat).createElement("RoleList");

		Iterator <String> privilegeList = vsGranted.listIterator();

		while (privilegeList.hasNext()) {
			p = privilegeList.next();
			nRole = (WebCatalog.docWebcat).createTextNode(p);
			eRole = (WebCatalog.docWebcat).createElement("Role");
			eRole.appendChild(nRole);
			eRole.setAttribute("access", "Granted");
			eRole.setAttribute("isOOTBRole", isOOTBRole(p)+"");
			eRoleList.appendChild(eRole);
		}
		privilegeList = vsDenied.listIterator();
		while (privilegeList.hasNext()) {
			p = privilegeList.next();
			nRole = (WebCatalog.docWebcat).createTextNode(p);
			eRole = (WebCatalog.docWebcat).createElement("Role");
			eRole.appendChild(nRole);
			eRole.setAttribute("access", "Denied");
			eRole.setAttribute("isOOTBRole", isOOTBRole(p)+"");
			eRoleList.appendChild(eRole);
		}

		ePriv.setAttribute("PrivilegeName", sPrivName);
		ePriv.appendChild(eRoleList);

		return ePriv;
	}

	/***
	 * 
	 * @param f
	 */
	public Privilege (File f, String sGroup) {
		if (f.canRead()) {
			privilegeAttrib = new PrivilegeAttribFile(f.toString());

			sPrivName = privilegeAttrib.getName(true);
			System.out.println("\tPrivilege: " + getName());

			privilegeDir = new File(privilegeAttrib.getAttribDir());
			if (!privilegeDir.canRead())
				privilegeDir = null;
			else
				readPrivileges();
		}
	}
}
