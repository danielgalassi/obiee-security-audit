/**
 * 
 */
package obiee.audit.webcat.core;


/**
 * This class represents the out of the box permissions and weighing values to encode user access level to OBI objects
 * @author danielgalassi@gmail.com
 *
 */
public class StandardSecuritySettings {

	/** name of OBIEE permissions */
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

	/** weighing values for out of the box permissions */
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

	/***
	 * Finds the internal position for a given permission
	 * @param param an OBIEE privilege name
	 * @return the index of the privilege or -1 if cannot be found
	 */
	public int getIndexForWeighing(String param) {
		for (int i=0; i<permissions.length; i++) {
			if (permissions[i].equals(param)) {
				return i;
			}
		}
		return -1;
	}

	public int getWeighingForIndex(int index) {
		return values[index];
	}

	public String getPermissionForIndex(int index) {
		return permissions[index];
	}

	/***
	 * Getter method
	 * @return the number of out of the box permissions
	 */
	public int size() {
		return values.length;
	}

	/***
	 * Finds the closest out of the box weighing value for a given aggregated permission value
	 * @param param an aggregated weighing value
	 * @return closest out of the box weighing value or 0.
	 * @throws Exception if weighing parameter is outside bounds
	 */
	public int matchClosestWeighingForValue(int param) throws Exception {
		if (param < 0 || param > 65535) {
			throw new Exception("Invalid permission encoded value");
		}
		for (int value : values) {
			if (param >= value) {
				return value;
			}
		}
		return 0;
	}

	/**
	 * Find the permission for given weighing value
	 * @param param weighing value
	 * @return the permission description or null if no permission is found for the passed value
	 */
	public String getPermissionForValue(int param) {
		int index = getIndex(param);
		if (index > -1) {
			return getPermissionForIndex(index);
		}
		return null;
	}
}
