/**
 * Severity
 */
package com.bs.bod.error;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;



/**
 * @author dbs on Dec 28, 2015 9:16:44 PM
 * @version 1.0
 * @since V0.0.2
 *
 */
public enum Severity implements Serializable, Iterable<Severity>{

  /**
   * for unknown
   */
  NONE, 
  TRACE, 
  DEBUG, 
  INFO, 
  WARNING, 
  ERROR, 
  CRITICAL, 
  FATAL;
  
  public static Severity find(String toSearch){
    if(StringUtils.isNotBlank(toSearch))
      for (Severity npt : Severity.values()) {
        if(/*toSearch.equalsIgnoreCase(npt.prefix) ||*/ toSearch.equalsIgnoreCase(npt.name()) /*|| toSearch.equalsIgnoreCase(npt.label)*/)
          return npt;
      }
    return Severity.NONE;
  }
  
  @Override
  public Iterator<Severity> iterator() {
    return new Iterator<Severity>() {

      private int index = 0;

      @Override
      public boolean hasNext() {

        int size = Severity.values().length;

        if (index >= size) {
          index = 0;// reinit
          return false; // end iteration
        }
        return index < size;
      }

      @Override
      public Severity next() {
        switch (index) {
        default:
          return Severity.values()[index++];
        }
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException("remove");
      }

    };
  }
}
