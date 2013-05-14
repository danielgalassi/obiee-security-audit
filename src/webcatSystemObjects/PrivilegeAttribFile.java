package webcatSystemObjects;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SuppressWarnings("serial")
public class PrivilegeAttribFile extends File {

	public String getName () {
		byte	b_data = 0;
		int		l = 0;
		String	sName = "";

		try {
			FileInputStream file_input = new FileInputStream (this);
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
				sName = sName + c;
			}

			sName = sName.replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2");

			data_in.close ();
		} catch  (IOException e) {
			e.printStackTrace();
		}

		return sName;
	}

	public String getAttribDir() {
		File f = null;
		try {
			f = new File(this.getCanonicalFile().toString().replace(".atr", ""));
			if (!f.canRead())
				return null;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return f.toString();
	}

	public PrivilegeAttribFile(String pathname) {
		super(pathname);
	}
}
