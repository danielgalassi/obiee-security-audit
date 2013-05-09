package webcatObjects;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PrivilegeSettingsFile {

	File fPriv;
	String sPrivFilename;

	public String getUnscrambledName() {
		String	sSAWName = "";
		byte	b_data = 0;
		int		l = 0;

		if (fPriv != null && fPriv.canRead())
			try {
				FileInputStream file_input = new FileInputStream (fPriv);
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
					sSAWName = sSAWName + c;
				}

				//making privilege names user friendly
				if (sSAWName.endsWith("Privs"))
					sSAWName = sSAWName.replace("Privs", "");
				if (sSAWName.startsWith("SA.\""))
					sSAWName = sSAWName.replace("SA.\"", "Subject Area \"");
				if (sSAWName.endsWith("System"))
					sSAWName = sSAWName.replace("System", "");
				if (sSAWName.startsWith("View."))
					sSAWName = sSAWName.replace("View.", "");
				if (sSAWName.endsWith("View"))
					sSAWName = sSAWName.replace("View", " View");
				if (sSAWName.endsWith("view"))
					sSAWName = sSAWName.replace("view", " View");
				if (sSAWName.charAt(0) == sSAWName.toLowerCase().charAt(0))
					sSAWName = (""+sSAWName.charAt(0)).toUpperCase()+sSAWName.substring(1);
				if (sSAWName.endsWith(" View"))
					sSAWName = "Add / Edit "+sSAWName;
				if (sSAWName.endsWith("Prompt"))
					sSAWName = "Add / Edit "+sSAWName.replaceAll("Prompt", " Prompt");

				data_in.close ();
			} catch  (IOException e) {
				e.printStackTrace();
			}
		return sSAWName.replaceAll("---", "-");
	}

	public PrivilegeSettingsFile (File f) {
		if (f.canRead()) {
			fPriv = f;
		}
	}
}
