package webcatSharedObjects;

import java.io.File;
import java.util.Vector;

import utils.PrivilegeAttribFile;

public class Report {

	private String sReportName = "";
	private File fReport;
	private Vector <Vector <String>> privileges;

	public String getName() {
		return sReportName;
	}

	public Report(File s) {
		if (s.canRead()) {
			fReport = s;
			PrivilegeAttribFile reportAttrib = new PrivilegeAttribFile(fReport+".atr");

			sReportName = reportAttrib.getName();

			System.out.print("(" + s.getName() + ")\t" +getName());
		}
	}
}
