
package com.douqu.game.close;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Encodes a string using MD5 hashing
 * 
 * @author Bean
 */
public class MD5Utils {

	/**
	 * Encodes a string
	 * 
	 * @param str String to encode
	 * @return Encoded String
	 * @throws java.security.NoSuchAlgorithmException
	 */
	public static String encode(String str) {
		if (str == null || str.length() == 0) {
			throw new IllegalArgumentException("String to encript cannot be null or zero length");
		}
		StringBuffer hexString = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte[] hash = md.digest();
			for (int i = 0; i < hash.length; i++) {
				if ((0xff & hash[i]) < 0x10) {
					hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
				} else {
					hexString.append(Integer.toHexString(0xFF & hash[i]));
				}
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hexString.toString();
	}
	
	public static String encodeLowercase(String str)
	{
		return encode(str).toLowerCase();
	}
	
	public static String encodeUppercase(String str)
	{
		return encode(str).toUpperCase();
	}
	

	public static void main(String[] args) {
		System.out.println(encode("123123"));
	}
	
}
