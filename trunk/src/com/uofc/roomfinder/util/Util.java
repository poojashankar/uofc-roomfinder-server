package com.uofc.roomfinder.util;

public class Util {

	/**
	 * help method to check if the String is numeric
	 * 
	 * @param input
	 *            string to check
	 * @return true = numeric, false != numeric
	 */
	public static boolean isNumeric(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
