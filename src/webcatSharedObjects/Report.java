package webcatSharedObjects;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import utils.PrivilegeAttribFile;

public class Report {

	private String sReportName = "";
	private File fReport;

	private static String getPrivilegeList(int val) {
		String sPermissions = "";
		HashMap <String, Integer> h = new HashMap <String, Integer> ();
		h.put ("Full Control",					65535);
		h.put ("View BIPublisher reports",		8192);
		h.put ("Schedule BIPublisher reports",	4096);
		h.put ("Run BIPublisher reports",		2048);
		h.put ("Set Ownership",					32);
		h.put ("Change Permissions",			16);
		h.put ("Modify",						15);
		h.put ("Delete",						8);
		h.put ("Write",							4);
		h.put ("Open",							3);
		h.put ("Traverse",						2);
		h.put ("Read",							1);
		h.put ("No Access",						0);

		return sPermissions;
	}

	private void getPrivileges() {
		File f = new File (fReport+".atr");
		FileInputStream file_input = null;
		DataInputStream data_in    = null;
		byte	b_data = 0;
		int l = 0;
		int iRead;
		int iGroupLength;
		int nGroups = 0;
		String y;
		String sOwner = "";

		System.out.println("\nProcessing: " + f);

		try {
			file_input = new FileInputStream(f);
			data_in = new DataInputStream (file_input);

			//looking for the length of the actual name
			for (int i = 0; i<8; i++) {
				b_data = data_in.readByte();
				if (i==4)
					l = b_data;
			}

			//skipping the bytes used for the name
			for (int i = 0; i<l; i++)
				b_data = data_in.readByte();

			b_data = data_in.readByte();
			while (b_data != 2)
				b_data = data_in.readByte();

			//length of the owner's name
			l = data_in.readByte();

			//skipping a few sterile bytes
			for (int i = 0; i<3; i++)
				b_data = data_in.readByte();

			//retrieving the name of the owner
			for (int i = 0; i<l; i++) {
				b_data = data_in.readByte();
				char c = (char)b_data;
				if (b_data < 0)
					c = '-';
				sOwner = sOwner + c;
			}
			System.out.println("Owner: " + sOwner);

			//reading the # of groups, first two bytes are overwritten since
			//they do not contain any data
			for (int i = 0; i<3; i++)
				nGroups = data_in.readByte();
			System.out.println("# of Groups: " + nGroups);

			for (int n=0; n<nGroups; n++) {
				//skipping bytes till the "size mark" (2) is found
				iRead = data_in.read();
				while (iRead != 2)
					iRead = data_in.read();

				iGroupLength = data_in.read();
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
				int val = data_in.readUnsignedByte() + data_in.readUnsignedByte() * 256;
				System.out.println(y + " --> " + val + "\t(" + getPrivilegeList(val) + ")");
			}

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
		}
	}
}
