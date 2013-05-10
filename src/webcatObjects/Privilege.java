package webcatObjects;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

public class Privilege {

	File fPrivName, fPriv;
	String sPrivName = "";
	Vector <String> vsGranted;

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

	public String getName () {
		return sPrivName;
	}

	public Privilege (File f) {
		if (f.canRead()) {
			fPrivName = f;
			setName();
			try {
				fPriv = new File(fPrivName.getCanonicalFile().toString().replace(".atr", ""));
				if (!fPriv.canRead())
					fPriv = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
