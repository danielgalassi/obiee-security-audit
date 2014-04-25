package obiee.audit.webcat.utils;

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

import obiee.audit.webcat.engine.WebCatalog;
import obiee.audit.webcat.objects.shared.Permission;

import org.w3c.dom.Document;
import org.w3c.dom.Node;


public class SharedObject {

	private static HashMap <Integer, String> hmVerbosePermissions = new HashMap <Integer, String> ();

	private static String getPrivilegeList(int val, String sPermissions) {
		int i = 0;
		//finding the highest permission for a cumulative 2-HEX value
		while (val < (WebCatalog.weighingValues).get(i) && i< (WebCatalog.weighingValues).size()) {
			i++;
		}

		//recursive call to concatenate the list of permissions
		if (val > 0 || (val == 0 && sPermissions.equals(""))) {
			val -= (WebCatalog.weighingValues).get(i);
			sPermissions += (WebCatalog.permissions).get(i);
			if (val > 0) {
				sPermissions += "; ";
				sPermissions = getPrivilegeList(val, sPermissions);
			}
		}
		return sPermissions;
	}

	public static Vector <Permission> getPrivileges(File sharedObject) {
		Vector <Permission> permissions = new Vector <Permission>();
		File attribute = new File (sharedObject+".atr");
		FileInputStream file_input = null;
		DataInputStream data_in    = null;
		byte b_data = 0;
		int l = 0;
		int iRead;
		int iGroupLength;
		int nGroups = 0;
		String role;

		try {
			file_input = new FileInputStream(attribute);
			data_in = new DataInputStream (file_input);

			//looking for the length of the actual name
			for (int i = 0; i<8; i++) {
				b_data = data_in.readByte();
				if (i==4)
					l = b_data;
			}

			//skipping the bytes used for the name
			for (int i = 0; i<l; i++)
				b_data = data_in.readByte();

			b_data = data_in.readByte();
			while (b_data != 2)
				b_data = data_in.readByte();

			//length of the owner's name
			l = data_in.readByte();

			//skipping a few sterile bytes
			for (int i = 0; i<3; i++)
				b_data = data_in.readByte();

			//skipping the bytes used for the name
			for (int i = 0; i<l; i++)
				b_data = data_in.readByte();

			//reading the # of groups, first two bytes are not used
			for (int i = 0; i<3; i++)
				nGroups = data_in.readByte();

			for (int n=0; n<nGroups; n++) {
				//skipping bytes till the "size mark" (2) is found
				iRead = data_in.read();
				while (iRead != 2)
					iRead = data_in.read();

				iGroupLength = data_in.read();
				//ignoring next three bytes
				for (int i=0; i<3; i++) {
					data_in.read();
				}

				role = "";
				for (int j = 0; j<iGroupLength; j++) {
					b_data = data_in.readByte();
					char c = (char)b_data;
					if (b_data < 0)
						c = '-';
					role = role + c;
				}
				int val = data_in.readUnsignedByte() + data_in.readUnsignedByte() * 256;

				if (!hmVerbosePermissions.containsKey(val)) {
					hmVerbosePermissions.put(val, getPrivilegeList(val, ""));
				}

				permissions.add(new Permission(role, val, hmVerbosePermissions.get(val)));
			}

			data_in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return permissions;
	}


	public static String getOwner(File sharedObject) {
		File f = new File (sharedObject+".atr");
		FileInputStream file_input = null;
		DataInputStream data_in    = null;
		byte	b_data = 0;
		int l = 0;
		String sOwner = "";

		try {
			file_input = new FileInputStream(f);
			data_in = new DataInputStream (file_input);

			//looking for the length of the actual name
			for (int i = 0; i<8; i++) {
				b_data = data_in.readByte();
				if (i==4)
					l = b_data;
			}

			//skipping the bytes used for the name
			for (int i = 0; i<l; i++)
				b_data = data_in.readByte();

			b_data = data_in.readByte();
			while (b_data != 2)
				b_data = data_in.readByte();

			//length of the owner's name
			l = data_in.readByte();

			//skipping a few sterile bytes
			for (int i = 0; i<3; i++)
				b_data = data_in.readByte();

			//retrieving the name of the owner
			for (int i = 0; i<l; i++) {
				b_data = data_in.readByte();
				char c = (char)b_data;
				if (b_data < 0)
					c = '-';
				sOwner = sOwner + c;
			}

			data_in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sOwner;
	}

	public static boolean isXML (File s) {
		boolean isXML = false;

		if (s.isFile() && s.canRead() && s.length() > 0 && (new File(s+".atr")).canRead()) {
			Scanner content = null;
			try {
				content = new Scanner(s);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if (content.hasNextLine()) {
				isXML = content.nextLine().contains("<?xml ");
			}
		}

		return isXML;
	}

	public static boolean isDashboard(File s) {
		Node nTag = null;
		if (isXML(s)) {
			Document docReport = XMLUtils.loadDocument(s);
			XPath xPath = XPathFactory.newInstance().newXPath();

			try {
				nTag = (Node) xPath.evaluate("/dashboard",
						docReport.getDocumentElement(),
						XPathConstants.NODE);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}

		}
		return (nTag != null);
	}

	public static boolean isReport(File s) {
		Node nTag = null;
		if (isXML(s)) {
			Document docReport = XMLUtils.loadDocument(s);
			XPath xPath = XPathFactory.newInstance().newXPath();

			try {
				nTag = (Node) xPath.evaluate("/report/@dataModel", docReport.getDocumentElement(), XPathConstants.NODE);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}

			//If a dataModel tag cannot be found, it is a good indicator that
			//an actual report or just a different type of object.
			if (nTag == null)
				try {
					nTag = (Node) xPath.evaluate("/report",
							docReport.getDocumentElement(),
							XPathConstants.NODE);
				} catch (XPathExpressionException e) {
					e.printStackTrace();
				}
			else nTag = null;

		}
		return (nTag != null);
	}

	public static boolean isPage(File s) {
		Node nTag = null;
		if (isXML(s)) {
			Document report = XMLUtils.loadDocument(s);
			XPath xPath = XPathFactory.newInstance().newXPath();

			try {
				nTag = (Node) xPath.evaluate("/dashboardPage", report.getDocumentElement(), XPathConstants.NODE);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}
		return (nTag != null);
	}
}
