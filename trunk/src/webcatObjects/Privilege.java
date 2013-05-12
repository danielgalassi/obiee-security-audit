package webcatObjects;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Privilege {

	private File privilegeAttribs, privilegeDir;
	private String sPrivName = "";
	private Vector <String> vsGranted;
	private Vector <String> vsDenied;

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

	/***
	 * 
	 */
	private void setName () {
		byte	b_data = 0;
		int		l = 0;

		try {
			FileInputStream file_input = new FileInputStream (privilegeAttribs);
			DataInputStream data_in    = new DataInputStream (file_input);

			//looking for the length of the actual name
			for (int i = 0; i<8; i++) {
				b_data = data_in.readByte();
				if (i==4)
					l = b_data;
			}

			//retrieving the name reading bytes,
			//converting them to a string
			for (int i = 0; i<l; i++) {
				b_data = data_in.readByte();
				char c = (char)b_data;
				if (b_data < 0)
					c = '-';
				sPrivName = sPrivName + c;
			}

			sPrivName = sPrivName.replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2");

			data_in.close ();
		} catch  (IOException e) {
			e.printStackTrace();
		}
	}

	/***
	 * 
	 * @return
	 */
	public String getName () {
		return sPrivName;
	}

	public Element serialize () {
		Element eRole;
		Node nRole;

		Element ePriv = (WebCatalog.docWebcat).createElement("Privilege");
		Element eRoleList = (WebCatalog.docWebcat).createElement("RoleList");

		for (int i=0; i<vsGranted.size(); i++) {
			nRole = (WebCatalog.docWebcat).createTextNode(vsGranted.get(i));
			eRole = (WebCatalog.docWebcat).createElement("Role");
			eRole.appendChild(nRole);
			eRole.setAttribute("access", "Granted");
			eRoleList.appendChild(eRole);
		}
		for (int i=0; i<vsDenied.size(); i++) {
			nRole = (WebCatalog.docWebcat).createTextNode(vsDenied.get(i));
			eRole = (WebCatalog.docWebcat).createElement("Role");
			eRole.appendChild(nRole);
			eRole.setAttribute("access", "Denied");
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
			privilegeAttribs = f;

			setName();

			try {
				privilegeDir = new File(privilegeAttribs.
						getCanonicalFile().toString().replace(".atr", ""));
				if (!privilegeDir.canRead())
					privilegeDir = null;
				else
					readPrivileges();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
