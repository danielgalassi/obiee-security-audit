package obiee.audit.webcat.objects.system;

import java.io.File;
import java.util.Vector;

import obiee.audit.webcat.core.PrivilegeAttribFile;
import obiee.audit.webcat.core.WebCatalog;
import obiee.audit.webcat.filters.AttributeFileFilter;

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

		Element e = serialize();
		for (File privilegeFile : componentDir.listFiles(new AttributeFileFilter())) {
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
		Element component = (WebCatalog.docWebcat).createElement("Component");
		component.setAttribute("ComponentName", privilegeGroupName);
		return component;
	}

	/**
	 * Constructor of each component.
	 * @param componentFile Attribute file storing the unscrambled name of the component.
	 */
	public Component (File componentFile) {
		if (componentFile.canRead()) {
			componentAttribute = new PrivilegeAttribFile(componentFile.toString());

			privilegeGroupName = componentAttribute.getName(true, 4);
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
