import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WebcatExposer {

	private static void getNewNameAndPermissions (int o, int p, FileInputStream file_input, String prof) {
		DataInputStream data_in    = new DataInputStream (file_input);
		byte	b_data = 0;
		int iGroupLength;

		try {
			for (int i = 0; i<o; i++)
				//System.out.print(
				data_in.read();
			//+" + ");

			iGroupLength = data_in.read();

			for (int i = 0; i<p; i++)
				//System.out.print(
						data_in.read();
						//);

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
			System.out.println("\n" + prof + " name = "+y+" "+data_in.read()+" "+data_in.read());
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String getSAWNameUnscrambled(File fWorkingNode) {
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

				data_in.close ();
			} catch  (IOException e) {
				e.printStackTrace();
			}
			return sSAWName.replaceAll("---", "-");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File f = new File("C:\\data\\workspace\\obiee-security-audit\\sampleCases\\test.atr");
		if (!f.canRead())
			System.out.println("Please check path.");
		System.out.println("Fancy Name: " + getSAWNameUnscrambled(f));
	}

}
