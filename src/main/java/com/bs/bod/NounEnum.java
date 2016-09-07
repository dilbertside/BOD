/**
 * NounEnum
 */
package com.bs.bod;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

/**
 * NounEnum is the object or document that is being acted upon
 * common ones are listed
 * 
 * @author dbs on Dec 25, 2015 11:13:35 AM
 * @version 1.0 initial
 * @since 0.0.1
 */
public enum NounEnum implements Serializable,Iterable<NounEnum> {
  //@formatter:off
  /**
   * no object
   */
  none               ("NO", "NONE"),
  /**
  * catch all non defined object
  */
  element            ("ELT", "Element"),
  purchaseOrder      ("PO", "Purchase Order"), 
  requestForQuote    ("RFQ", "Request For Quote"), 
  invoice            ("INV", "Invoice"),
  project            ("PRJ", "Project"),
  product            ("PRD", "Product"),
  artefact           ("ART", "Artefact"),
  hardware           ("HW", "Hardware"),
  ;
  //@formatter:on
  private String label;
  private String identifier;

  NounEnum(String identifier, String label) {
    this.identifier = identifier;
    this.label = label;
  }

  public static NounEnum find(String toSearch) {
    if (StringUtils.isNotBlank(toSearch))
      for (NounEnum npt : NounEnum.values()) {
        if (toSearch.equalsIgnoreCase(npt.identifier) || toSearch.equalsIgnoreCase(npt.name()) || toSearch.equalsIgnoreCase(npt.label))
          return npt;
      }
    return none;
  }

  /**
   * @return the identifier
   */
  String getIdentifier() {
    return identifier;
  }

  /**
   * @return the label
   */
  String getLabel() {
    return label;
  }

  @Override
  public Iterator<NounEnum> iterator() {
    return new Iterator<NounEnum>() {

      private int index = 0;

      @Override
      public boolean hasNext() {

        int size = NounEnum.values().length;

        if (index >= size) {
          index = 0;// reinit
          return false; // end iteration
        }
        return index < size;
      }

      @Override
      public NounEnum next() {
        switch (index) {
        default:
          return NounEnum.values()[index++];
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
