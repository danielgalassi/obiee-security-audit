package obiee.audit.webcat.objects.system;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;

import obiee.audit.webcat.engine.WebCatalog;
import obiee.audit.webcat.utils.PrivilegeAttribFile;

import org.w3c.dom.Element;


/**
 * Low-level privileges are grouped into Components.
 * @author danielgalassi@gmail.com
 *
 */
public class Component {

	private PrivilegeAttribFile componentAttribute = null;
	private File componentDir = null;
	private String privilegeGroupName = "";
	private Vector <Privilege> privileges;

	/**
	 * Analyses the list of privileges for each component.
	 */
	private void setPrivs () {
		privileges = new Vector <Privilege>();

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
		for (File privilegeFile : componentDir.listFiles(filter)) {
			Privilege privilege = new Privilege(privilegeFile, getName());
			privileges.add(privilege);
			e.appendChild(privilege.serialize());
			(WebCatalog.eCompList).appendChild(e);
		}

	}

	/***
	 * Returns the name of this Component.
	 * @return the component name.
	 */
	public String getName () {
		return privilegeGroupName;
	}

	/***
	 * Creates an XML structure for the component.
	 * @return <code>Element</code> with the Component tag with a ComponentName attribute.
	 */
	public Element serialize () {
		Element eGroup = (WebCatalog.docWebcat).createElement("Component");
		eGroup.setAttribute("ComponentName", privilegeGroupName);
		return (eGroup);
	}

	/**
	 * Constructor of each component.
	 * @param componentFile Attribute file storing the unscrambled name of the component.
	 */
	public Component (File componentFile) {
		if (componentFile.canRead()) {
			componentAttribute = new PrivilegeAttribFile(componentFile.toString());

			privilegeGroupName = componentAttribute.getName(true, 4);
			//System.out.println("Component:" + getName());
			componentDir = new File(componentAttribute.getAttribDir());
			if (!componentDir.canRead()) {
				componentDir = null;
			}
			else {
				setPrivs();
			}
		}
	}
}
