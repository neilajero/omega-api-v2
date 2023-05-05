package com.ejb.exception.ad;

import com.util.Debug;

public class AdBACoaGlCashAccountNotFoundException extends Exception {

   public AdBACoaGlCashAccountNotFoundException() {
      Debug.print("AdBACoaGlCashAccountNotFoundException Constructor");
   }

   public AdBACoaGlCashAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("AdBACoaGlCashAccountNotFoundException Constructor");
   }
}
