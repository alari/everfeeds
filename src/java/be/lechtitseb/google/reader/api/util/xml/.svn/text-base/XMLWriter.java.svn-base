
package be.lechtitseb.google.reader.api.util.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * XML Writer helper class based on JDOM
 */
public class XMLWriter {
	private static final Logger logger =
			Logger.getLogger(XMLWriter.class.getName());

	/**
	 * Write an XML document to a file
	 * 
	 * @param returnValue
	 *        The document to write to disk
	 * @param destinationFile
	 *        The file where to write it
	 * @return The saved document
	 * @throws IOException
	 */
	public Document write(Document returnValue, File destinationFile)
			throws IOException {
		if (returnValue == null) {
			throw new IllegalArgumentException("The document cannot be null!");
		}
		if (destinationFile == null) {
			throw new IllegalArgumentException(
					"The destination file cannot be null!");
		}
		OutputStreamWriter oStream = null;
		try {
			//logger.debug("Writing XML file to: "
			//		+ destinationFile.getAbsolutePath());
			XMLOutputter outputter = new XMLOutputter();
			outputter.setFormat(Format.getPrettyFormat());
			outputter.getFormat().setEncoding("UTF-8");
			oStream =
					new OutputStreamWriter(
							new FileOutputStream(destinationFile), "UTF-8");
			// writer = new FileWriter(destinationFile);
			// outputter.output(returnValue, writer);
			outputter.output(returnValue, oStream);
			returnValue.setBaseURI(destinationFile.getPath());
		} catch (IOException e1) {
			throw e1;
		} finally {
			if (oStream != null) {
				try {
					oStream.close();
				} catch (IOException e1) {
					logger
							.debug("Error while closing the output stream after writing the xml file: "
									+ e1);
				}
			}
		}
		return returnValue;
	}
}