package webcatSharedObjects;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import utils.PrivilegeAttribFile;

public class Report {

	private String sReportName = "";
	private File fReport;
	private Vector <Vector <String>> privileges;

	private void getPrivileges() {
		File f = new File (fReport+".atr");
		FileInputStream file_input = null;
		DataInputStream data_in    = null;
		byte	b_data = 0;
		int iRead;
		int iGroupLength;
		int nGroups = 0;
		String y;

		System.out.println("\nProcessing: " + f);

		try {
			file_input = new FileInputStream(f);
			data_in = new DataInputStream (file_input);

			//reading the # of groups in this file, stored in the 4th byte
			for (int i = 0; i<8; i++) {
				b_data = data_in.readByte();
				System.out.println(b_data);
				if (i==4)
					nGroups = b_data;
			}

			System.out.println("# of Groups: " + nGroups);

			
			/*
			for (int n=0; n<nGroups; n++) {

				//skipping bytes till the "size mark" (2) is found
				iRead = data_in.read();
				while (iRead != 2)
					iRead = data_in.read();

				iGroupLength = data_in.read();
				System.out.print("Group name length: " + iGroupLength + "\t");

				//ignoring next three bytes
				for (int i=0; i<3; i++)
					data_in.read();

				y = "";
				for (int j = 0; j<iGroupLength; j++) {
					b_data = data_in.readByte();
					char c = (char)b_data;
					if (b_data < 0)
						c = '-';
					y = y + c;
				}
				System.out.println(y + " --> " + data_in.read());

			}*/

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getName() {
		return sReportName;
	}

	public Report(File s) {
		if (s.canRead()) {
			fReport = s;
			PrivilegeAttribFile reportAttrib = new PrivilegeAttribFile(fReport+".atr");

			sReportName = reportAttrib.getName();

			System.out.print("(" + s.getName() + ")\t" +getName());
			getPrivileges();
			System.out.println("Leaving report.");
		}
	}
}
