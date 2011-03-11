
package be.lechtitseb.google.reader.api.util.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * XML Reader helper class based on JDOM
 */
public class XMLReader {
	private SAXBuilder builder;

	/**
	 * Create a new XMLReader
	 */
	public XMLReader() {
		builder = new SAXBuilder();
		//builder.setValidation(true);
	}

	/**
	 * Read an XML file
	 * 
	 * @param f
	 *        The XML file to read
	 * @return The read document
	 * @throws FileNotFoundException
	 *         If the file wasn't found
	 * @throws JDOMException
	 *         If there was a problem while reading the content (malformed or
	 *         incorrect document)
	 * @throws IOException
	 *         If there was a problem while reading the file
	 * @throws IllegalArgumentException
	 *         if the file object is null or is not a file but a folder
	 */
	public Document readFile(File f) throws FileNotFoundException,
			JDOMException, IOException {
		checkReceivedFileObject(f);
		return builder.build(f);
	}
	
	public Document read(String xml) throws JDOMException, IOException {
		Reader stringReader = new StringReader(xml);
		return builder.build(stringReader);
		
	}
	
	

	/**
	 * Check if the file is not null, exists and is a file
	 * 
	 * @param f
	 *        The file to check
	 * @throws FileNotFoundException
	 *         If the file could not be found
	 * @throws IllegalArgumentException
	 *         if the given file object is null or if it is not a file but a
	 *         folder
	 */
	private void checkReceivedFileObject(File f) throws FileNotFoundException {
		if (f == null) {
			throw new IllegalArgumentException("The file cannot be null!");
		}
		if (!f.exists()) {
			throw new FileNotFoundException("The file does not exist: "
					+ f.getAbsolutePath());
		}
		if (!f.isFile()) {
			throw new IllegalArgumentException("The file: "
					+ f.getAbsolutePath() + " is not a file! (folder?)");
		}
	}
	
	/**
	 * Create a JDOM Document from an InputStream
	 * @param is The inputstream to read the xml from
	 * @return The read document
	 * @throws JDOMException If the InputStream does not contain a valid document
	 * @throws IOException If there was a problem while reading the document
	 * @throws IllegalArgumentException If the InputStream is null
	 */
	public Document readInputStream(InputStream is) throws JDOMException, IOException {
		if (is == null) {
			throw new IllegalArgumentException("The inputstream cannot be null!");
		}
		try {
			return builder.build(is);
		}finally{
			is.close();
		}
	}

	/**
	 * Read an xml file as a string
	 * 
	 * @param f
	 *        The file to read
	 * @return The file as a string
	 * @throws FileNotFoundException
	 *         If the file was not found
	 * @throws JDOMException
	 *         If there is a problem with the xml in the file (invalid document)
	 * @throws IOException
	 *         If there was a problem while reading the file
	 * @throws IllegalArgumentException
	 *         If the file is a folder or if the file object is null
	 */
	public String readFileAsString(File f) throws FileNotFoundException,
			JDOMException, IOException {
		checkReceivedFileObject(f);
		return new SAXBuilder().build(f).toString();
	}
}
