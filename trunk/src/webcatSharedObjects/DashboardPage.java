package webcatSharedObjects;

import java.io.File;

import utils.PrivilegeAttribFile;

public class DashboardPage {

	private File fPage = null;
	private String sPageName = "";

	public DashboardPage(File file) {
		fPage = file;
		PrivilegeAttribFile pageAttrib = new PrivilegeAttribFile(file+".atr");
		sPageName = pageAttrib.getName();
		System.out.println("\t\t\t" + sPageName);

	}

}
