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

		System.out.println("Privilege audit in progress...");
		if (wc != null && isPrivilegeAuditInvoked)
			wc.processWebCatPrivileges();
		System.out.println("Privilege audit completed.");

		System.out.println("Dashboard audit in progress...");
		if (wc != null && isDashboardAuditInvoked)
			wc.processDashboards();
		System.out.println("Dashboard Audit completed.");

		wc.save();
	}
}
