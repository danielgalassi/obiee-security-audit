package webcatSharedObjects;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import utils.PrivilegeAttribFile;
import webcatAudit.WebCatalog;

public class Report {

	private String	sReportName = "";
	private String	sOwner;
	private File	fReport;
	private Vector <Permission> vPerms;

	private String getPrivilegeList(int val, String sPermissions) {
		int i = 0;
		//finding the highest permission for a cumulative 2-HEX value
		while (val < (WebCatalog.n).get(i) && i< (WebCatalog.n).size())
			i++;

		//recursive call to concatenate the list of permissions
		if (val > 0 || (val == 0 && sPermissions.equals(""))) {
			val -= (WebCatalog.n).get(i);
			sPermissions += (WebCatalog.p).get(i);
			if (val > 0) {
				sPermissions += "; ";
				sPermissions = getPrivilegeList(val, sPermissions);
			}
		}
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
		String sRole;

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

			//reading the # of groups, first two bytes are overwritten since
			//they do not contain any data
			for (int i = 0; i<3; i++)
				nGroups = data_in.readByte();

			for (int n=0; n<nGroups; n++) {
				//skipping bytes till the "size mark" (2) is found
				iRead = data_in.read();
				while (iRead != 2)
					iRead = data_in.read();

				iGroupLength = data_in.read();
				//ignoring next three bytes
				for (int i=0; i<3; i++)
					data_in.read();

				sRole = "";
				for (int j = 0; j<iGroupLength; j++) {
					b_data = data_in.readByte();
					char c = (char)b_data;
					if (b_data < 0)
						c = '-';
					sRole = sRole + c;
				}
				int val = data_in.readUnsignedByte() + data_in.readUnsignedByte() * 256;
				vPerms.add(new Permission(sRole, val, getPrivilegeList(val, "")));
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
			vPerms = new Vector <Permission> ();
			getPrivileges();
		}
	}
}
