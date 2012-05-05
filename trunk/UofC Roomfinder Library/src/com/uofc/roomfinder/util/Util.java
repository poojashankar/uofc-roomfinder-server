package com.uofc.roomfinder.util;

public class Util {

	/**
	 * fills up a string from the left
	 * 
	 * @param str
	 * @param length
	 * @param car
	 * @return
	 */
	public static String lPad(String str, Integer length, char car) {
		return str + String.format("%" + (length - str.length()) + "s", "").replace(" ", String.valueOf(car));
	}

	/**
	 * fills up a string from the right
	 * 
	 * @param str
	 * @param length
	 * @param car
	 * @return
	 */
	public static String rPad(String str, Integer length, char car) {
		return String.format("%" + (length - str.length()) + "s", "").replace(" ", String.valueOf(car)) + str;
	}

	
	/**
	 * returns only numeric chars in a string
	 * 
	 * @param str
	 * @return
	 */
	public static String getOnlyNumerics(String str) {

		if (str == null) {
			return null;
		}

		StringBuffer strBuff = new StringBuffer();
		char c;

		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);

			if (Character.isDigit(c)) {
				strBuff.append(c);
			}
		}
		return strBuff.toString();
	}

}
