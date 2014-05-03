/**
 * 
 */
package obiee.audit.webcat.filters;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Accepts files other than attributes
 * @author danielgalassi@gmail.com
 *
 */
public class ExcludeAttributeFileFilter implements FilenameFilter {

	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	@Override
	public boolean accept(File dir, String name) {
		File file = new File (dir, name);
		if (file.canRead() && file.isFile() && !name.endsWith(".atr")) {
			return true;
		}
		return false;
	}

}
