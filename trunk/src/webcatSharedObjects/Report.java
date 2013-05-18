package webcatSharedObjects;

import java.io.File;

public class Report {

	public boolean isReport () {
		return false;
	}

	public Report(File s) {
		System.out.print("Report:" + s);
	}
}
