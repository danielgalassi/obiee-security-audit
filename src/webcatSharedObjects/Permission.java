package webcatSharedObjects;

public class Permission {

	private String	sRole;
	private int		iPermValue;
	private String	sPermList;

	public void list() {
		System.out.println(sRole + ": " + iPermValue + "\t(" + sPermList + ")");
	}

	public Permission(String rol, int val, String lst) {
		sRole = rol;
		iPermValue = val;
		sPermList = lst;
		//System.out.println(sRole + ": " + iPermValue + "\t(" + sPermList + ")");
	}
}
