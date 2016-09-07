/**
 * ConfirmationCode
 */
package com.bs.bod;

import java.io.Serializable;

/**
 * The Confirmation Code request is an option controlled by the Sender business application.  
 * It is a request to the receiving application to send back a confirmation BOD to the sender. 
 * The confirmation Business Object Document may indicate the successful processing of the original Business Object Document 
 * or return error conditions if the original Business Object Document was unsuccessful.
 * @author dbs on Dec 25, 2015 11:13:35 AM
 * @version 1.0
 * @since 0.0.1
 *
 */
public enum ConfirmationCode implements Serializable{
  /**
   * No confirmation Business Object Document requested
   * <br> F&F Fire and Forget
   */
  never,
  /**
   * OnError send back a confirmation Business Object Document only if an error has occurred
   * <br> F&E Fire and Error
   */
  onError,
  /**
   * Always send a confirmation Business Object Document regardless
   * <br> F&R Fire and Reply
   */
  always
}
