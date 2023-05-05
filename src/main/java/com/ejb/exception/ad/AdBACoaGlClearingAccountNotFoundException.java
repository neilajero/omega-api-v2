package com.ejb.exception.ad;

import com.util.Debug;

public class AdBACoaGlClearingAccountNotFoundException extends Exception {

   public AdBACoaGlClearingAccountNotFoundException() {
      Debug.print("AdBACoaGlClearingAccountNotFoundException Constructor");
   }

   public AdBACoaGlClearingAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("AdBACoaGlClearingAccountNotFoundException Constructor");
   }
}
