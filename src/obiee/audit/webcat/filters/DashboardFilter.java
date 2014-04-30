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
public class DashboardFilter implements FilenameFilter {

	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	@Override
	public boolean accept(File dir, String name) {
		File f = new File (dir, name + "\\_portal");

		if (f.isDirectory() && f.canRead()) {
			if (f.listFiles().length > 0) {
				return true;
			}
		}
		return false;
	}

}
