/**
 * 
 */
package obiee.audit.webcat.objects.system;

import org.w3c.dom.Element;

/**
 * This interface contains standard methods to be implemented to describe OBIEE accounts
 * @author danielgalassi@gmail.com
 *
 */
public interface OBIAccount {

	public Element serialize();
	public String getID();
	public String getName();

}
