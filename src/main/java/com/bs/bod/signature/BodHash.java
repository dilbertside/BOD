/**
 * BodHash
 */
package com.bs.bod.signature;

/**
 * interface to implement to give more latitude for generating a customized digest not based on default POJO properties values<br> 
 * @author dbs on Aug 20, 2016 10:50:05 PM
 * @since 0.2.0
 * @version 1.0.0
 *
 */
public interface BodHash {
	/**
	 * Implement it to build customized hash to create a digest checksum BOD payload
	 * @param key private Key shared by sender and receiver cf {@link com.bs.bod.Bod#__sharedPrivateKey}
	 * @return customized string to be used for checksum 
	 */
	public String hash(final String privateKey);
	
}
