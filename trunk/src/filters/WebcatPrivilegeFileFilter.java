/**
 * 
 */
package filters;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author danielgalassi@gmail.com
 *
 */
public class WebcatPrivilegeFileFilter implements FilenameFilter {

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
