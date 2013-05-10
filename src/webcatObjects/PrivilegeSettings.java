package webcatObjects;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

public class PrivilegeSettings {

	File fPrivATR, fPrivDir;
	String sSAWName = "";
	Vector <Privilege> vPrivs;

	public String getName () {
		return sSAWName;
	}
	
	private void setName() {
		byte	b_data = 0;
		int		l = 0;

		try {
			FileInputStream file_input = new FileInputStream (fPrivATR);
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
			if (sSAWName.startsWith("MarketingSystem"))
				sSAWName = sSAWName.replace("MarketingSystem", "");
			if (sSAWName.endsWith("privs"))
				sSAWName = sSAWName.replace("privs", "");
			if (sSAWName.startsWith("SA.\""))
				sSAWName = sSAWName.replace("SA.\"", "Subject Area \"");
			if (sSAWName.startsWith("general"))
				sSAWName = sSAWName.replace("general", "Access");
			if (sSAWName.endsWith("System"))
				sSAWName = sSAWName.replace("System", "");
			if (sSAWName.startsWith("View."))
				sSAWName = sSAWName.replace("View.", "");
			if (sSAWName.startsWith("Search"))
				sSAWName = sSAWName.replace("Search", "Answers");
			if (sSAWName.endsWith("View"))
				sSAWName = sSAWName.replace("View", " View");
			if (sSAWName.endsWith("view"))
				sSAWName = sSAWName.replace("view", " View");
			if (sSAWName.charAt(0) == sSAWName.toLowerCase().charAt(0))
				sSAWName = (""+sSAWName.charAt(0)).toUpperCase()+sSAWName.substring(1);
			if (sSAWName.endsWith("Prompt"))
				sSAWName = sSAWName.replaceAll("Prompt", " Prompt");

			data_in.close ();
		} catch  (IOException e) {
			e.printStackTrace();
		}
	}

	public PrivilegeSettings(File f) {
		if (f.canRead()) {
			fPrivATR = f;
			try {
				fPrivDir = new File(fPrivATR.getCanonicalFile().toString().replace(".atr", ""));
				if (fPrivDir.canRead())
					fPrivDir = null;
				setName();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
