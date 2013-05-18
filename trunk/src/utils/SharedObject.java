package utils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SharedObject {

	public static boolean isXML (File s) {
		byte	b_data = 0;
		String	sName = "";

		if (s.canRead() && (new File(s+".atr")).canRead())
			try {
				FileInputStream file_input = new FileInputStream (s);
				DataInputStream data_in    = new DataInputStream (file_input);

				//retrieving the name reading bytes,
				//converting them to a string
				for (int i = 0; i<5; i++) {
					b_data = data_in.readByte();
					char c = (char)b_data;
					sName = sName + c;
				}

				data_in.close ();
			} catch  (IOException e) {
				e.printStackTrace();
			}

		return (sName.equals("<?xml"));
	}

}
