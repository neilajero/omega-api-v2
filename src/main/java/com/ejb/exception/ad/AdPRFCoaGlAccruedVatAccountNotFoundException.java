package com.ejb.exception.ad;

import com.util.Debug;

public class AdPRFCoaGlAccruedVatAccountNotFoundException extends Exception {

   public AdPRFCoaGlAccruedVatAccountNotFoundException() {
      Debug.print("AdPRFCoaGlAccruedVatAccountNotFoundException Constructor");
   }

   public AdPRFCoaGlAccruedVatAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("AdPRFCoaGlAccruedVatAccountNotFoundException Constructor");
   }
}
