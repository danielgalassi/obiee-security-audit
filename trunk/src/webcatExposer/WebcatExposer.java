package webcatExposer;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import webcatObjects.WebCatalog;

public class WebcatExposer {

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
				//System.out.println(y);
			}

			int val = 0;
			System.out.println("\n" + prof + " name1 = "+y+" ");

			val = data_in.readUnsignedByte() + data_in.readUnsignedByte() * 256;
			System.out.println(val);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

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

	private static void readPrivileges (File f) {
		FileInputStream file_input = null;
		DataInputStream data_in    = null;
		byte	b_data = 0;
		int iRead;
		int iGroupLength;
		int nGroups;
		String y;

		System.out.println("Starting...");

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
				System.out.println("Group name length: " + iGroupLength);

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

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void processWebCatPrivileges(File fWebCatLocation) {
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if(name.lastIndexOf('.')>0)
				{
					int lastIndex = name.lastIndexOf('.');
					String str = name.substring(lastIndex);
					if(str.equals(".atr"))
						return true;
				}
				return false;
			}
		};

		System.out.println("Only .atr (Privs) files...");
		String[] sd = fWebCatLocation.list(filter);
		for (int i=0; i<sd.length; i++) {
			System.out.println(sd[i] + "\t\t-->\t" + getSAWNameUnscrambled(new File(fWebCatLocation.getPath() + "\\" + sd[i]), true));
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		WebCatalog wc = null;
		String sWebcatLocation = null;

		//picking up parameters
		for (int a=0; a<args.length; a++)
			//Web Catalogue Location
			if (args[a].startsWith("-w="))
				sWebcatLocation = args[a].replaceAll("-w=", "");

		if (sWebcatLocation != null) {
			wc = new WebCatalog(sWebcatLocation);
			wc.processWebCatPrivileges();
//			processWebCatPrivileges(wc.getPrivilegesDirectory());
		}

/*
		File f = new File(".\\sampleCases\\answers.atr");
		if (!f.canRead())
			System.out.println("Please check path.");
		//System.out.println("Fancy Name: " + getSAWNameUnscrambled(f));
		readPrivileges(f);

		f = new File(".\\sampleCases\\myaccountprivs.atr");
		System.out.println("\n\n----------\nPrivs file");
		System.out.println(getSAWNameUnscrambled(f, true));
*/
	}
}
