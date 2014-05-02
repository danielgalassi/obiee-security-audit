/**
 * 
 */
package obiee.audit.webcat.objects.system;

import org.w3c.dom.Element;

/**
 * @author danielgalassi@gmail.com
 *
 */
public interface OBIAccount {

	public Element serialize();
	public String getID();
	public String getName();

}
