/**
 * ErrorEnum
 */
package com.bs.bod.error;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

/**
 * Java defines two kinds of exceptions:
 * 
 * Checked exceptions: Exceptions that inherit from the Exception class are checked exceptions. Client code has to handle the checked exceptions thrown by the
 * API, either in a catch clause or by forwarding it outward with the throws clause.
 * 
 * Unchecked exceptions: RuntimeException also extends from Exception. However, all of the exceptions that inherit from RuntimeException get special treatment.
 * There is no requirement for the client code to deal with them, and hence they are called unchecked exceptions.
 * 
 * @author dbs on Dec 26, 2015 10:21:13 PM
 * @since V0.0.1
 * @version 1.0
 * @version 1.1 add {@value #technicalDetail}, {@value #functionalDetail}, {@link #exceptionDetail}
 *
 */
public enum ErrorType implements Serializable, Iterable<ErrorType>{

  none(BodError.TYPE_NONE, ""),
  /**
   * java exception or programmatic
   */
  exception(BodError.TYPE_EXCEPTION, "Programmatic"),
  /**
   * technical or resource
   */
  technical(BodError.TYPE_TECHNICAL, "Technical"),
  /**
   * functional or business
   */
  functional(BodError.TYPE_FUNCTIONAL, "Functional"),
  
  /**
   * java exception or programmatic used to add stacktrace or extra very long message not for end-user consumption
   */
  exceptionDetail(BodError.TYPE_EXCEPTION_DETAIL, "Programmatic detailed"),
  /**
   * technical or resource used to add stacktrace or extra very long message not for end-user consumption
   */
  technicalDetail(BodError.TYPE_TECHNICAL_DETAIL, "Technical detailed"),
  /**
   * functional or business used to add stacktrace or extra very long message not for end-user consumption
   */
  functionalDetail(BodError.TYPE_FUNCTIONAL_DETAIL, "Functional detailed");
  
  long mask;
  String label;
  
  ErrorType(long mask, String label){
    this.mask = mask;
    this.label = label;
  }

  /**
   * @return the mask
   */
  public long getMask() {
    return mask;
  }
  
  /**
   * @return the label
   */
  public String getLabel() {
    return label;
  }

  public static ErrorType find(long toSearch){
    for (ErrorType npt : ErrorType.values()) {
      if((toSearch & npt.mask) == npt.mask)
        return npt;
    }
    return none;
  }
  
  public static ErrorType find(String toSearch) {
    if (StringUtils.isNotBlank(toSearch))
      for (ErrorType npt : ErrorType.values()) {
        if ( toSearch.equalsIgnoreCase(npt.name()) || toSearch.equalsIgnoreCase(npt.label))
          return npt;
      }
    return none;
  }
  @Override
  public Iterator<ErrorType> iterator() {
    return new Iterator<ErrorType>() {

      private int index = 0;

      @Override
      public boolean hasNext() {

        int size = ErrorType.values().length;

        if (index >= size) {
          index = 0;// reinit
          return false; // end iteration
        }
        return index < size;
      }

      @Override
      public ErrorType next() {
        switch (index) {
        default:
          return ErrorType.values()[index++];
        }
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException("remove");
      }

    };
  }

  /* (non-Javadoc)
   * @see java.lang.Enum#toString()
   */
  @Override
  public String toString() {
    return getLabel();
  }
  
}
