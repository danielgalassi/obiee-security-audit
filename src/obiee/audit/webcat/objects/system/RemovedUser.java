package obiee.audit.webcat.objects.system;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import obiee.audit.webcat.core.WebCatalog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

/**
 * This class represents a removed OBIEE user account. Each user is identified by a name and an ID.
 * @author danielgalassi@gmail.com
 *
 */
public class RemovedUser implements OBIAccount {

	private static final Logger logger = LogManager.getLogger(RemovedUser.class.getName());

	private String   id;
	private String name;

	public Element serialize() {
		Element user = (WebCatalog.docWebcat).createElement("User");
		user.setAttribute("UserName", name);
		user.setAttribute("UserID", id);
		return user;
	}

	public String getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	private String getValueFromMark(DataInputStream data_in) throws IOException {
		String temp = "";
		int length = data_in.readByte();
		data_in.skipBytes(3);
		for (int i = 0; i < length; i++) {
			byte b_data = data_in.readByte();
			char c = (char)b_data;
			if (b_data < 0) {
				c = '-';
			}
			temp = temp + c;
		}
		return temp;
	}

	private void getDetails(File removedUserFile) {
		FileInputStream file_input = null;
		DataInputStream data_in    = null;
		byte b_data = 0;

		try {
			file_input = new FileInputStream(removedUserFile);
			data_in = new DataInputStream (file_input);

			data_in.skipBytes(1);
			//discarding bytes until the mark (2) is reached
			while (b_data != 2) {
				b_data = data_in.readByte();
			}

			id = getValueFromMark(data_in);

			name = "(removed account) " + getValueFromMark(data_in);

			data_in.close();
		} catch (IOException e) {
			logger.error("{} thrown while processing a shared object", e.getClass().getCanonicalName());
		}
	}

	public RemovedUser(File removedUserFile) {
		getDetails(removedUserFile);
	}
}
