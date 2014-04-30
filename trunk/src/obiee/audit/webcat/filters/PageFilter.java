/**
 * 
 */
package obiee.audit.webcat.filters;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author danielgalassi@gmail.com
 *
 */
public class PageFilter implements FilenameFilter {

	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	@Override
	public boolean accept(File dir, String name) {
		if (!name.endsWith(".atr") && !name.equals("dashboard+layout")) { 
			return true;
		}

		return false;
	}

}
