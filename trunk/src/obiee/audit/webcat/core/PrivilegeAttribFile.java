package obiee.audit.webcat.core;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("serial")
public class PrivilegeAttribFile extends File {

	private static final Logger logger = LogManager.getLogger(PrivilegeAttribFile.class.getName());

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

			//retrieving the name reading bytes, converting them to a string
			for (int i = 0; i<l; i++) {
				b_data = data_in.readByte();
				char c = (char)b_data;
				if (b_data < 0) {
					c = '-';
				}
				name = name + c;
			}

			if (applyFormatting) {
				name = name.replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2");
			}

			data_in.close ();
		} catch  (IOException e) {
			logger.error("{} thrown while retrieving a privilege name", e.getClass().getCanonicalName());
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
			logger.error("{} thrown while processing a privilege file", e.getClass().getCanonicalName());
		}

		return f.toString();
	}

	public PrivilegeAttribFile(String pathname) {
		super(pathname);
	}
}
