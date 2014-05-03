/**
 * 
 */
package obiee.audit.webcat.filters;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Accepts attribute files, unless they are DVT files.
 * @author danielgalassi@gmail.com
 *
 */
public class AttributeFileFilter implements FilenameFilter {

	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	@Override
	public boolean accept(File dir, String name) {
		if(name.lastIndexOf('.') > 0) {
			int lastIndex = name.lastIndexOf('.');
			String str = name.substring(lastIndex);
			if(str.equals(".atr") && !name.contains("dvt")) {
				return true;
			}
		}
		return false;
	}

}
