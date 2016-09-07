/**
 * MessageDigestBuilder
 */
package com.bs.bod.signature;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.bs.bod.Bod;

/**
 * @author dbs on Aug 20, 2016 11:09:48 PM
 * @since 0.2.0
 * @version 1.0.0
 *
 */
public class MessageDigestBuilder {

	/**
	 * Encodes the given string into a sequence of bytes using the UTF-8 charset and calculates the SHA-1 digest and returns the value as a hex string.
	 * @param input string to encode
	 * @return the SHA-1 digest of the input string cf https://en.wikipedia.org/wiki/SHA-1
	 */
	static public String hash(String input){
		return DigestUtils.sha1Hex(input);
	}

	/**
	 * Encodes the given string into a sequence of bytes using the UTF-8 charset and calculates the SHA-1 digest and returns the value as a hex string.<br>
	 * encoding rule:<br>
	 * if payload implement {@link BodHash} method hash will be called<br>
	 * if not extended from {@link BodHash}, an introspection will be made on the object properties and a concatenation of values will be extracted, value separator is , (comma)
	 * 
	 * @param input {@link Object} to encode
	 * @return the SHA-1 digest of the input string cf https://en.wikipedia.org/wiki/SHA-1
	 */
	public static String hash(Object input) {
		if(input  instanceof BodHash ){
			return DigestUtils.sha1Hex(((BodHash)input).hash(Bod.getSharedPrivateKey()));
		}
		return DigestUtils.sha1Hex(ReflectionToStringBuilder.toString(input, ToStringStyle.SIMPLE_STYLE));
	}
}
