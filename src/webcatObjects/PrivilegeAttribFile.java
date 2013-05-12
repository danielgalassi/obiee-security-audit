package webcatObjects;

import java.io.File;
import java.io.IOException;

public class PrivilegeAttribFile extends File {

	public PrivilegeAttribFile(String pathname) {
		super(pathname);
	}

	public String getAttribDir() {
		File f = null;
		try {
			f = new File(this.getCanonicalFile().toString().replace(".atr", ""));
			if (!f.canRead())
				return null;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return f.toString();
	}
}
