/**
 * VerbEnum
 */
package com.bs.bod;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;


/**
 * Verbs – VerbEnum identifies the action being performed on the specific Noun of the BOD.
 * @author dbs on Dec 25, 2015 11:13:35 AM
 * @version 1.0
 * @since 0.0.1
 */
public enum VerbEnum implements Serializable, Iterable<VerbEnum>{
  /**
   * equivalent to no action, none, etc
   */
  none,
  ping,
  ack,
  create,
  get,
  list,
  update,
  delete,
  add,
  process, 
  cancel, 
  sync,
  post,
  /**
   * If an error occurs in the processing of a BOD in the receiving application and the Sender set the Confirmation to either OnError or Always. 
   * The receiving application must provide a ConfirmBOD that references the original BODs BODId. 
   * This ConfirmBOD indicates that there was an error in the original BOD and carries the error messages from the receiving system.
   */
  confirm,
  show,
  load,
  error,
  close,
  push,
  pull,
  /**
   * used for command control, adapters or else, typical sub commnand will be start, stop, status<br>
   * additional info will be set in the field {@link Verb#control}
   */
  control
  ;
  
  public static VerbEnum find(String toSearch){
    if(StringUtils.isNotBlank(toSearch))
      for (VerbEnum npt : VerbEnum.values()) {
        if(/*toSearch.equalsIgnoreCase(npt.prefix) ||*/ toSearch.equalsIgnoreCase(npt.name()) /*|| toSearch.equalsIgnoreCase(npt.label)*/)
          return npt;
      }
    return none;
  }
  
  @Override
  public Iterator<VerbEnum> iterator() {
    return new Iterator<VerbEnum>() {

      private int index = 0;

      @Override
      public boolean hasNext() {

        int size = VerbEnum.values().length;

        if (index >= size) {
          index = 0;// reinit
          return false; // end iteration
        }
        return index < size;
      }

      @Override
      public VerbEnum next() {
        switch (index) {
        default:
          return VerbEnum.values()[index++];
        }
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException("remove");
      }

    };
  }
}
