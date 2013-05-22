package utils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;


public class SharedObject {

	public static boolean isXML (File s) {
		byte	b_data = 0;
		String	sName = "";

		if (s.canRead() && s.length() > 0 && (new File(s+".atr")).canRead())
			try {
				FileInputStream file_input = new FileInputStream (s);
				DataInputStream data_in    = new DataInputStream (file_input);

				//retrieving the name reading bytes,
				//converting them to a string
				for (int i = 0; i<5; i++) {
					b_data = data_in.readByte();
					char c = (char)b_data;
					sName = sName + c;
				}

				data_in.close ();
			} catch  (IOException e) {
				e.printStackTrace();
			}

		return (sName.startsWith("<?xml"));
	}

	public static boolean isDashboard(File s) {
		Node nTag = null;
		if (isXML(s)) {
			Document docReport = XMLUtils.File2Document(s);
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
			Document docReport = XMLUtils.File2Document(s);
			XPath xPath = XPathFactory.newInstance().newXPath();

			try {
				nTag = (Node) xPath.evaluate("/report/@dataModel",
						docReport.getDocumentElement(),
						XPathConstants.NODE);
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
			Document docReport = XMLUtils.File2Document(s);
			XPath xPath = XPathFactory.newInstance().newXPath();

			try {
				nTag = (Node) xPath.evaluate("/dashboardPage",
						docReport.getDocumentElement(),
						XPathConstants.NODE);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}
		return (nTag != null);
	}
}
