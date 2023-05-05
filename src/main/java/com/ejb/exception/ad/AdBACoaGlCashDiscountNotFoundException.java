package com.ejb.exception.ad;

import com.util.Debug;

public class AdBACoaGlCashDiscountNotFoundException extends Exception {

   public AdBACoaGlCashDiscountNotFoundException() {
      Debug.print("AdBACoaGlCashDiscountNotFoundException Constructor");
   }

   public AdBACoaGlCashDiscountNotFoundException(String msg) {
      super(msg);
      Debug.print("AdBACoaGlCashDiscountNotFoundException Constructor");
   }
}
