package webcatSystemObjects;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;

import org.w3c.dom.Element;

import utils.PrivilegeAttribFile;
import webcatAudit.WebCatalog;

/**
 * Low-level privileges are grouped into Components.
 * @author danielgalassi@gmail.com
 *
 */
public class Component {

	private PrivilegeAttribFile ComponentAttrib = null;
	private File fComponentDir = null;
	private String sPrivGroupName = "";
	private Vector <Privilege> vPrivs;

	/**
	 * Analyses the list of privileges for each component.
	 */
	private void setPrivs () {
		vPrivs = new Vector <Privilege>();

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

		Element e = serialize();
		for (File fPrivilege : fComponentDir.listFiles(filter)) {
			Privilege p = new Privilege(fPrivilege, getName());
			vPrivs.add(p);
			e.appendChild(p.serialize());
			(WebCatalog.eCompList).appendChild(e);
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
	 * Creates an XML structure for the component.
	 * @return <code>Element</code> with the Component tag with a ComponentName attribute.
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
			ComponentAttrib = new PrivilegeAttribFile(f.toString());

			sPrivGroupName = ComponentAttrib.getName(true,4);
			System.out.println("Component:" + getName());
			fComponentDir = new File(ComponentAttrib.getAttribDir());
			if (!fComponentDir.canRead())
				fComponentDir = null;
			else
				setPrivs();
		}
	}
}
