package webcatAudit;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class WebcatAudit {

	private static boolean isPrivilegeAuditInvoked = false;
	private static boolean isDashboardAuditInvoked = false;

	private static void getPrivileges2(File fReport) {
		File f = fReport;
		FileInputStream file_input = null;
		DataInputStream data_in    = null;
		byte	b_data = 0;
		int iRead;
		int l = 0;
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
				int val = data_in.readUnsignedByte() + data_in.readUnsignedByte() * 256;
				System.out.println(y + " --> " + val);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/***
	 * 
	 * @param o
	 * @param p
	 * @param file_input
	 * @param prof
	 */
	private static void getNewNameAndPermissions (int o, int p, FileInputStream file_input, String prof) {
		DataInputStream data_in    = new DataInputStream (file_input);
		byte	b_data = 0;
		int iGroupLength;

		try {
			for (int i = 0; i<o; i++)
				data_in.read();

			iGroupLength = data_in.read();

			for (int i = 0; i<p; i++)
				data_in.read();

			String y = "";
			//Concatenating Group / User name
			for (int i = 0; i<iGroupLength; i++) {
				b_data = data_in.readByte();
				char c = (char)b_data;
				if (b_data < 0)
					c = '-';
				y = y + c;
			}

			int val = 0;
			System.out.println("\n" + prof + " name1 = "+y+" ");

			val = data_in.readUnsignedByte() + data_in.readUnsignedByte() * 256;
			System.out.println(val);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	public static String getSAWNameUnscrambled(File fWorkingNode, boolean isPrivsFile) {
		File	fSAW = null;
		String	sSAWName = "";
		byte	b_data = 0;
		int		l = 0;
		int		nGroups = 0;

		//checking the .atr file, where the long/Answers name is stored
		try {
			fSAW = new File(fWorkingNode.getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (fSAW != null && fSAW.canRead())
			try {
				FileInputStream file_input = new FileInputStream (fSAW);
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

				//trimming the Privs suffix
				if (isPrivsFile) {
					if (sSAWName.endsWith("Privs"))
						sSAWName = sSAWName.substring(0, (sSAWName.length()-5));
					if (sSAWName.startsWith("SA.\""))
						sSAWName = sSAWName.replace("SA.\"", "Subject Area \"");
				}

				if (!isPrivsFile) {
					//getting the owner of this object
					getNewNameAndPermissions (3, 3, file_input, "(Owner)");

					//looking for the number of groups / users
					nGroups = data_in.readByte();

					System.out.println(nGroups + " groups / users found.");

					int iOffset = 2;
					for (int iGCounter = 0; iGCounter < nGroups; iGCounter++) {
						if (iGCounter == 1)
							iOffset++;
						getNewNameAndPermissions (iOffset, 3, file_input,"Group "+(iGCounter+1));
					}
				}
				data_in.close ();
			} catch  (IOException e) {
				e.printStackTrace();
			}
		return sSAWName.replaceAll("---", "-");
	}
	 */

	/***
	 * 
	 * @param f
	 */
	private static void readPrivileges (File f) {
		FileInputStream file_input = null;
		DataInputStream data_in    = null;
		byte	b_data = 0;
		int iRead;
		int iGroupLength;
		int nGroups;
		String y;

		System.out.println("\nStarting...");

		try {
			file_input = new FileInputStream(f);
			data_in = new DataInputStream (file_input);

			//ignoring first two bytes
			for (int i = 0; i<2; i++) {
				data_in.read();
			}

			//reading the number of groups granted access in this file
			nGroups = data_in.read();
			System.out.println("# of Groups: " + nGroups);

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
					System.out.println(b_data);
					if (b_data < 0)
						c = '-';
					y = y + c;
				}
				System.out.println(y + " --> " + data_in.read());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		WebCatalog wc = null;
		String sWebcatLocation = null;

		//picking up parameters
		for (int a=0; a<args.length; a++) {
			//Web Catalog Location
			if (args[a].startsWith("-w="))
				sWebcatLocation = args[a].replaceAll("-w=", "");
			if (args[a].startsWith("-privs"))
				isPrivilegeAuditInvoked = true;
			if (args[a].startsWith("-dashboards"))
				isDashboardAuditInvoked = true;
		}

		if (sWebcatLocation != null)
			wc = new WebCatalog(sWebcatLocation);
		/*
		if (wc != null && isPrivilegeAuditInvoked)
			wc.processWebCatPrivileges();

		if (wc != null && isDashboardAuditInvoked)
			wc.processDashboards();
		 */
		wc.save();

		File f = new File(".\\sampleCases\\sample1.atr");
		if (!f.canRead())
			System.out.println("Please check path.");
		else
			getPrivileges2(f);
	}
}
