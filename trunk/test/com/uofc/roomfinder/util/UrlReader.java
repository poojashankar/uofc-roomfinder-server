package com.uofc.roomfinder.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.Charset;

public class UrlReader {

	/**
	 * 
	 * @param stringUrl
	 * @return a string with the content of the page
	 */
	public static String readFromURL(String stringUrl) {
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
		int cp;

		try {
			// escape URL
			stringUrl = stringUrl.replace("http://", "");
			String hostname = stringUrl.split("/")[0];
			String path = stringUrl.replace(hostname, "");

			// cut off port (if set)
			if (hostname.indexOf(':') != -1) {
				hostname = hostname.substring(0, hostname.indexOf(':'));
			}

			// open stream for URL
			is = new URI("http", null, hostname, 8080, path, null, null).toURL().openStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

			// read whole page
			while ((cp = rd.read()) != -1) {
				sb.append((char) cp);
			}
			return sb.toString();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
			}
		}
		return null;
	}
}
