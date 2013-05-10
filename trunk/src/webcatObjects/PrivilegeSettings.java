package webcatObjects;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Vector;

public class PrivilegeSettings {

	private File fPrivATR = null;
	private File fPrivDir = null;
	private String sPrivGroupName = "";
	private Vector <Privilege> vPrivs;

	/***
	 * 
	 */
	private void setPrivs () {
		vPrivs = new Vector <Privilege>();
		File[] fPrivList;

		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if(name.lastIndexOf('.')>0)
				{
					int lastIndex = name.lastIndexOf('.');
					String str = name.substring(lastIndex);
					//selecting only attribute files
					if(str.equals(".atr") && name.indexOf("dvt") == -1)
						return true;
				}
				return false;
			}
		};

		fPrivList = fPrivDir.listFiles(filter);
		for (int i=0; i<fPrivList.length; i++)
			vPrivs.add(new Privilege(fPrivList[i], getName()));

	}

	/***
	 * 
	 * @return
	 */
	public String getDirectory () {
		return fPrivDir.toString();
	}

	/***
	 * 
	 * @return
	 */
	public String getName () {
		return sPrivGroupName;
	}

	/***
	 * 
	 */
	private void setName () {
		byte	b_data = 0;
		int		l = 0;

		try {
			FileInputStream file_input = new FileInputStream (fPrivATR);
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
				sPrivGroupName = sPrivGroupName + c;
			}

			data_in.close ();
		} catch  (IOException e) {
			e.printStackTrace();
		}
	}

	/***
	 * 
	 * @param f
	 */
	public PrivilegeSettings (File f) {
		if (f.canRead()) {
			fPrivATR = f;
			setName();
			System.out.println(sPrivGroupName);
			try {
				fPrivDir = new File(fPrivATR.getCanonicalFile().toString().replace(".atr", ""));
				if (!fPrivDir.canRead())
					fPrivDir = null;
				else
					setPrivs();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
