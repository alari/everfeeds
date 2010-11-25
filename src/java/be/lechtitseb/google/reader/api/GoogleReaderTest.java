package be.lechtitseb.google.reader.api;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

import be.lechtitseb.google.reader.api.core.GoogleReader;
import be.lechtitseb.google.reader.api.model.exception.AuthenticationException;
import be.lechtitseb.google.reader.api.model.exception.GoogleReaderException;

/**
 * http://code.google.com/intl/ru-RU/apis/accounts/docs/AuthForInstalledApps.html#AuthProcess
 * http://code.google.com/intl/ru-RU/apis/accounts/docs/OAuth.html
 * @author gizatullinm
 *
 */
public class GoogleReaderTest {

	@Test
	public void testAuth() throws FileNotFoundException, IOException, AuthenticationException, GoogleReaderException {
		Properties auth = new Properties();
		auth.load( new FileInputStream("auth.properties"));

		GoogleReader googleReader = new GoogleReader(auth.getProperty("auth.key"),auth.getProperty("auth.secret"));
		googleReader.login();
		googleReader.getUserInformation();
		
	}
	
	public static void main(String[] args) {
		GoogleReaderTest test = new GoogleReaderTest();
		try {
			test.testAuth();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
