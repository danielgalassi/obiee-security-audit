/**
 * 
 */
package obiee.audit.webcat.core;


/**
 * @author danielgalassi@gmail.com
 *
 */
public class StandardSecuritySettings {

	private static final String[] permissions = 
		{"Full Control",
		"View BIPublisher reports",
		"Schedule BIPublisher reports",
		"Run BIPublisher reports",
		"Set Ownership",
		"Change Permissions",
		"Modify",
		"Delete",
		"Write",
		"Open",
		"Traverse",
		"Read",
		"No Access"};

	private static final int[] values =
		{65535,
		8192,
		4096,
		2048,
		32,
		16,
		15,
		8,
		4,
		3,
		2,
		1,
		0};

	public int getIndex(int param) {
		for(int i=0; i<values.length; i++) {
			if (values[i] == param) {
				return i;
			}
		}
		return -1;
	}

	public int getIndex(String param) {
		for (int i=0; i<permissions.length; i++) {
			if (permissions[i].equals(param)) {
				return i;
			}
		}
		return -1;
	}

	public int getWeighing(int index) {
		return values[index];
	}

	public String getPermission(int index) {
		return permissions[index];
	}

	public int size() {
		return values.length;
	}

	public int matchClosestWeighingWith(int param) {
		for (int value : values) {
			if (param >= value) {
				return value;
			}
		}
		return 0;
	}

	public String matchPermissionWith(int param) {
		return permissions[getIndex(param)];
	}
}
