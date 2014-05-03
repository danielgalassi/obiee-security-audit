/**
 * 
 */
package obiee.audit.webcat.filters;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Accepts files (except for attribute files) and directories
 * @author danielgalassi@gmail.com
 *
 */
public class ExcludeAttributeObjectFilter implements FilenameFilter {

	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	@Override
	public boolean accept(File dir, String name) {
		if (!name.endsWith(".atr")) {
			return true;
		}
		return false;
	}

}
