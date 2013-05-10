package webcatObjects;

import java.io.File;
import java.io.IOException;

public class Privilege {

	File fPrivATR, fPrivDir;
	
	public Privilege(File f) {
		if (f.canRead()) {
			fPrivATR = f;
			try {
				fPrivDir = new File(fPrivATR.getCanonicalFile().toString().replace(".atr", ""));
				if (fPrivDir.canRead())
					fPrivDir = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
