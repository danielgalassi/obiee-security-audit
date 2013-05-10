package webcatObjects;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

public class Privilege {

	private File fPrivName, fPriv;
	private String sPrivName = "";
	private Vector <String> vsGranted;
	private Vector <String> vsDenied;

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
			file_input = new FileInputStream(fPriv.toString());
			data_in = new DataInputStream (file_input);

			//ignoring first two bytes
			for (int i = 0; i<2; i++) {
				data_in.read();
			}

			//reading the number of groups granted access in this file
			nGroups = data_in.read();
			//System.out.println("# of Groups: " + nGroups);

			for (int n=0; n<nGroups; n++) {

				//skipping bytes till the "size mark" (2) is found
				iRead = data_in.read();
				while (iRead != 2)
					iRead = data_in.read();

				iGroupLength = data_in.read();
				//System.out.println("Group name length: " + iGroupLength);

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
			FileInputStream file_input = new FileInputStream (fPrivName);
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

			//making privilege names user friendly
			if (sPrivName.startsWith("Global "))
				sPrivName = sPrivName.replaceAll("Global ", "Access to ");

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

	/***
	 * 
	 * @param f
	 */
	public Privilege (File f) {
		if (f.canRead()) {
			fPrivName = f;
			setName();
			System.out.println("\t" + sPrivName);
			try {
				fPriv = new File(fPrivName.getCanonicalFile().toString().replace(".atr", ""));
				if (!fPriv.canRead())
					fPriv = null;
				else
					readPrivileges();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
