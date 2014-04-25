package obiee.audit.webcat.utils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SuppressWarnings("serial")
public class PrivilegeAttribFile extends File {

	public String getName (boolean applyFormatting, int offset) {
		byte	b_data = 0;
		int		l = 0;
		String	name = "";

		try {
			FileInputStream file_input = new FileInputStream (this);
			DataInputStream data_in    = new DataInputStream (file_input);

			//looking for the length of the actual name
			for (int i = 0; i<(offset+4); i++) {
				b_data = data_in.readByte();
				if (i==offset) {
					l = b_data;
				}
			}

			//retrieving the name reading bytes,
			//converting them to a string
			for (int i = 0; i<l; i++) {
				b_data = data_in.readByte();
				char c = (char)b_data;
				if (b_data < 0)
					c = '-';
				name = name + c;
			}

			if (applyFormatting)
				name = name.replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2");

			data_in.close ();
		} catch  (IOException e) {
			e.printStackTrace();
		}

		return name;
	}

	public String getAttribDir() {
		File f = null;
		try {
			f = new File(this.getCanonicalFile().toString().replace(".atr", ""));
			if (!f.canRead()) {
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return f.toString();
	}

	public PrivilegeAttribFile(String pathname) {
		super(pathname);
	}
}
