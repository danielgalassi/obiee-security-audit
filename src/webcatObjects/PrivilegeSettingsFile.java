package webcatObjects;

import java.io.File;

public class PrivilegeSettingsFile {

	File fPriv;

	public PrivilegeSettingsFile (File f) {
		if (f.canRead()) {
			fPriv = f;
		}
	}
}
