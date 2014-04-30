/**
 * 
 */
package obiee.audit.webcat.core;

import java.util.Vector;

/**
 * @author danielgalassi@gmail.com
 *
 */
public class StandardSecuritySettings {

	private static final Vector <String>    permissions = new Vector <String> ();
	private static final Vector <Integer>      weighing = new Vector <Integer> ();

	public StandardSecuritySettings() {
		permissions.add ("Full Control");
		permissions.add ("View BIPublisher reports");
		permissions.add ("Schedule BIPublisher reports");
		permissions.add ("Run BIPublisher reports");
		permissions.add ("Set Ownership");
		permissions.add ("Change Permissions");
		permissions.add ("Modify");
		permissions.add ("Delete");
		permissions.add ("Write");
		permissions.add ("Open");
		permissions.add ("Traverse");
		permissions.add ("Read");
		permissions.add ("No Access");
		weighing.add (65535);
		weighing.add (8192);
		weighing.add (4096);
		weighing.add (2048);
		weighing.add (32);
		weighing.add (16);
		weighing.add (15);
		weighing.add (8);
		weighing.add (4);
		weighing.add (3);
		weighing.add (2);
		weighing.add (1);
		weighing.add (0);
	}

	public Integer getWeighing(int i) {
		return weighing.get(i);
	}

	public String getPermission(int i) {
		return permissions.get(i);
	}

	public int size() {
		return permissions.size();
	}
}
