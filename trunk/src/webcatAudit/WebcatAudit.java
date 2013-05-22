package webcatAudit;

public class WebcatAudit {

	private static boolean isPrivilegeAuditInvoked = false;
	private static boolean isDashboardAuditInvoked = false;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		WebCatalog wc = null;
		String sWebcatLocation = null;

		//picking up parameters
		for (int a=0; a<args.length; a++) {
			//Web Catalog Location
			if (args[a].startsWith("-w="))
				sWebcatLocation = args[a].replaceAll("-w=", "");
			if (args[a].startsWith("-privs"))
				isPrivilegeAuditInvoked = true;
			if (args[a].startsWith("-dashboards"))
				isDashboardAuditInvoked = true;
		}

		if (sWebcatLocation != null)
			wc = new WebCatalog(sWebcatLocation);
System.out.println(wc.hmAllReports.containsKey("/shared/BI Operations/Reports/Report 001 - UsageTracking - # of Users per Hour"));
		//if (wc != null && isPrivilegeAuditInvoked)
		//	wc.processWebCatPrivileges();

		if (wc != null && isDashboardAuditInvoked)
			wc.processDashboards();

		wc.save();
	}
}
