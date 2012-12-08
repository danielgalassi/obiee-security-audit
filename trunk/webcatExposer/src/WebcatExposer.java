import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class WebcatExposer {

	private static void getNewNameAndPermissions (int o, int p, FileInputStream file_input, String prof) {
		DataInputStream data_in    = new DataInputStream (file_input);
		byte	b_data = 0;
		int x;

		try {
			for (int i = 0; i<o; i++)
				//System.out.print(
				data_in.read();
			//+" + ");

			x = data_in.read();
			//System.out.print("\t\tGroup length = "+x+"\t\t");
			for (int i = 0; i<p; i++)
				//System.out.print(
						data_in.read();
						//);

			String y = "";
			for (int i = 0; i<x; i++) {
				b_data = data_in.readByte();
				char c = (char)b_data;
				if (b_data < 0)
					c = '-';
				y = y + c;
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
		int		n = 0;

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
				n = data_in.readByte();

				System.out.println("Reading " + n + " groups / users.");

				getNewNameAndPermissions (2, 3, file_input,"Group 1");
				getNewNameAndPermissions (3, 3, file_input,"Group 2");
				getNewNameAndPermissions (3, 3, file_input,"Group 3");
				getNewNameAndPermissions (3, 3, file_input,"Group 4");
				getNewNameAndPermissions (3, 3, file_input,"Group 5");
				getNewNameAndPermissions (3, 3, file_input,"Group 6");
				getNewNameAndPermissions (3, 3, file_input,"Group 7");
				getNewNameAndPermissions (3, 3, file_input,"Group 8");
				
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
		File f = new File("/data/workspace/webcatExposer/shared/test.atr");
		System.out.println("Fancy Name: " + getSAWNameUnscrambled(f));
	}

}
