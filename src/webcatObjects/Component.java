package webcatObjects;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Vector;

import org.w3c.dom.Element;

/**
 * Low-level privileges are grouped into Components.
 * @author danielgalassi@gmail.com
 *
 */
public class Component {

	private File fPrivATR = null;
	private File fPrivDir = null;
	private String sPrivGroupName = "";
	private Vector <Privilege> vPrivs;

	/**
	 * Analyses the list of privileges for each component.
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
		Element e = serialize();
		for (int i=0; i<fPrivList.length; i++) {
			vPrivs.add(new Privilege(fPrivList[i], getName()));
			e.appendChild(vPrivs.get(i).serialize());
			(WebCatalog.eWebcat).appendChild(e);
		}

	}

	/***
	 * Returns the name of this Component.
	 * @return the component name.
	 */
	public String getName () {
		return sPrivGroupName;
	}

	/***
	 * Retrieves the actual name of the Component from the attribute file
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
	 * Creates an XML structure for the component.
	 * @return Element with the Component tag with a ComponentName attribute.
	 */
	public Element serialize () {
		Element eGroup = (WebCatalog.docWebcat).createElement("Component");
		eGroup.setAttribute("ComponentName", sPrivGroupName);

		return (eGroup);
	}


	/**
	 * Constructor of each component.
	 * @param f Attribute file storing the unscrambled name of the component.
	 */
	public Component (File f) {
		if (f.canRead()) {
			fPrivATR = f;

			setName();
			System.out.println(getName());
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
