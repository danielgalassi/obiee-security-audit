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
		for (int i=0; i<fPrivList.length; i++) {
			vPrivs.add(new Privilege(fPrivList[i]));
		}

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

			//making privilege names user friendly
			/*
			if (sPrivGroupName.endsWith("Privs"))
				sPrivGroupName = sPrivGroupName.replace("Privs", "");
			if (sPrivGroupName.startsWith("MarketingSystem"))
				sPrivGroupName = sPrivGroupName.replace("MarketingSystem", "(Marketing) ");
			if (sPrivGroupName.endsWith("privs"))
				sPrivGroupName = sPrivGroupName.replace("privs", "");
			if (sPrivGroupName.startsWith("SA.\""))
				sPrivGroupName = sPrivGroupName.replace("SA.\"", "Subject Area \"");
			if (sPrivGroupName.startsWith("general"))
				sPrivGroupName = sPrivGroupName.replace("general", "Access");
			if (sPrivGroupName.endsWith("System"))
				sPrivGroupName = sPrivGroupName.replace("System", "");
			if (sPrivGroupName.startsWith("View.")) {
				if (sPrivGroupName.endsWith("iew"))
					sPrivGroupName = sPrivGroupName.substring(0, (sPrivGroupName.length()-4));
				sPrivGroupName = sPrivGroupName.replace("View.", "View: ");
			}
			if (sPrivGroupName.startsWith("Search"))
				sPrivGroupName = sPrivGroupName.replace("Search", "Answers");
			if (sPrivGroupName.charAt(0) == sPrivGroupName.toLowerCase().charAt(0))
				sPrivGroupName = (""+sPrivGroupName.charAt(0)).toUpperCase()+sPrivGroupName.substring(1);
			if (sPrivGroupName.endsWith("Prompt"))
				sPrivGroupName = sPrivGroupName.replace("Prompt", " Prompt");
			*/

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
