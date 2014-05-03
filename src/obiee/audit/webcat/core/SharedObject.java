package obiee.audit.webcat.core;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import obiee.audit.webcat.objects.shared.Permission;
import obiee.audit.webcat.utils.XMLUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * This class represents objects (at a high level) stored under /root/shared
 * @author danielgalassi@gmail.com
 *
 */
public class SharedObject {

	private static final Logger logger = LogManager.getLogger(SharedObject.class.getName());

	private static HashMap <Integer, String> verbosePermissions = new HashMap <Integer, String> ();

	/**
	 * Gets a verbose description of privileges by reversing the weighing value. This method is called recursively.
	 * The method validates that the weighing value passed as argument is within range (0-65535).
	 * @param value weighing value attributed to a set of privileges granted on an object
	 * @param permission verbose description of privileges granted
	 * @return verbose description of privileges granted
	 * @throws Exception if weighing value is invalid
	 */
	public static String getPrivilegeList(int value, String permission) throws Exception {
		int nearestValue = (WebCatalog.ootbSecurity).matchClosestWeighingForValue(value);

		//recursive call to concatenate the list of permissions
		if (value > 0 || (value == 0 && permission.equals(""))) {
			value -= nearestValue;
			permission += (WebCatalog.ootbSecurity).getPermissionForValue(nearestValue);
			if (value > 0) {
				permission += "; ";
				permission = getPrivilegeList(value, permission);
			}
		}
		return permission;
	}

	/**
	 * Retrieves the list of privileges granted on the shared object. OBIEE privileges are stored in "attribute" files.
	 * These files are identified by their "atr" extension. 
	 * @param sharedObject file representing the actual shared object (not its attributes)
	 * @return list of privileges
	 */
	public static Vector <Permission> getPrivileges(File sharedObject) {
		Vector <Permission> permissions = new Vector <Permission>();
		File attribute = new File (sharedObject+".atr");
		FileInputStream file_input = null;
		DataInputStream data_in    = null;
		byte b_data = 0;
		int l = 0;
		int groupNameLength;
		int groupCount = 0;
		String role;

		try {
			file_input = new FileInputStream(attribute);
			data_in = new DataInputStream (file_input);

			//looking for the length of the actual name
			for (int i = 0; i<8; i++) {
				b_data = data_in.readByte();
				if (i==4) {
					l = b_data;
				}
			}

			//skipping the bytes used for the name
			data_in.read(new byte[l+1]);

			//skipping bytes till the "size mark" (2) is found
			while (data_in.readByte() != 2) ;

			//length of the owner's name
			l = data_in.readByte();

			//skipping a few sterile bytes
			data_in.read(new byte[3]);

			//skipping the bytes used for the name
			data_in.read(new byte[l]);

			//reading the # of groups, first two bytes are not used
			for (int i = 0; i<3; i++) {
				groupCount = data_in.readByte();
			}

			for (int n=0; n<groupCount; n++) {

				//skipping bytes till the "size mark" (2) is found
				while (data_in.read() != 2) ;

				groupNameLength = data_in.read();
				//ignoring next three bytes
				data_in.read(new byte[3]);

				role = "";
				for (int j = 0; j<groupNameLength; j++) {
					b_data = data_in.readByte();
					char c = (char)b_data;
					if (b_data < 0) {
						c = '-';
					}
					role = role + c;
				}
				int val = data_in.readUnsignedByte() + data_in.readUnsignedByte() * 256;

				if (!verbosePermissions.containsKey(val)) {
					String verbose;
					try {
						verbose = getPrivilegeList(val, "");
					} catch (Exception e) {
						logger.error("{} thrown while capturing the verbose description of a privilege ({})", e.getCause(), e.getMessage());
						verbose = "A description could not be generated. Please review this object in your OBI instance";
					}
					verbosePermissions.put(val, verbose);
				}

				permissions.add(new Permission(role, val, verbosePermissions.get(val)));
			}

			data_in.close();
		} catch (IOException e) {
			logger.error("{} thrown while processing a shared object", e.getClass().getCanonicalName());
		}
		return permissions;
	}

	/**
	 * Evaluates whether the shared object is a metadata (XML) file
	 * @param file a shared directory or file
	 * @return true if the file contents are in XML format
	 */
	public static boolean isXML (File file) {
		String line = "";

		if (file.isFile() && file.canRead() && file.length() > 0 && (new File(file+".atr")).canRead()) {
			Scanner content = null;
			try {
				content = new Scanner(file);
			} catch (FileNotFoundException e) {
				logger.error("{} thrown while attempting to validate the contents of a shared object", e.getClass().getCanonicalName());
			}
			if (content.hasNextLine()) {
				line = content.nextLine();
			}
		}

		return line.contains("<?xml ");
	}

	/**
	 * Evaluates whether the shared object is an OBIEE dashboard
	 * @param file a shared directory or file
	 * @return true if the file represents a dashboard
	 */
	public static boolean isDashboard(File file) {
		Node tag = null;
		if (isXML(file)) {
			Document docReport = XMLUtils.loadDocument(file);
			XPath xPath = XPathFactory.newInstance().newXPath();

			try {
				tag = (Node) xPath.evaluate("/dashboard", docReport.getDocumentElement(), XPathConstants.NODE);
			} catch (XPathExpressionException e) {
				logger.error("{} thrown while pre-processing a dashboard", e.getClass().getCanonicalName());
			}

		}
		return (tag != null);
	}

	/**
	 * Evaluates whether the shared object is an OBIEE analysis
	 * @param file a shared directory or file
	 * @return true if the file represents an analysis
	 */
	public static boolean isReport(File file) {
		Node tag = null;
		if (isXML(file)) {
			Document docReport = XMLUtils.loadDocument(file);
			XPath xPath = XPathFactory.newInstance().newXPath();

			try {
				tag = (Node) xPath.evaluate("/report/@dataModel", docReport.getDocumentElement(), XPathConstants.NODE);
			} catch (XPathExpressionException e) {
				logger.error("{} thrown while pre-processing a report", e.getClass().getCanonicalName());
			}

			//If a dataModel tag cannot be found, it is a good indicator that an actual report or just a different type of object.
			if (tag == null) {
				try {
					tag = (Node) xPath.evaluate("/report", docReport.getDocumentElement(), XPathConstants.NODE);
				} catch (XPathExpressionException e) {
					logger.error("{} thrown while pre-processing a report", e.getClass().getCanonicalName());
				}
			}
			else {
				tag = null;
			}

		}
		return (tag != null);
	}

	/**
	 * Evaluates whether a shared object is an OBIEE dashboard page
	 * @param file a shared directory or file 
	 * @return true if the file represents a dashboard page
	 */
	public static boolean isPage(File file) {
		Node tag = null;
		if (isXML(file)) {
			Document report = XMLUtils.loadDocument(file);
			XPath xPath = XPathFactory.newInstance().newXPath();
			try {
				tag = (Node) xPath.evaluate("/dashboardPage", report.getDocumentElement(), XPathConstants.NODE);
			} catch (XPathExpressionException e) {
				logger.error("{} thrown while pre-processing a dashboard page", e.getClass().getCanonicalName());
			}
		}
		return (tag != null);
	}
}
