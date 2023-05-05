package com.ejb.exception.ad;

import com.util.Debug;

public class AdBACoaGlSalesDiscountNotFoundException extends Exception {

   public AdBACoaGlSalesDiscountNotFoundException() {
      Debug.print("AdBACoaGlSalesDiscountNotFoundException Constructor");
   }

   public AdBACoaGlSalesDiscountNotFoundException(String msg) {
      super(msg);
      Debug.print("AdBACoaGlSalesDiscountNotFoundException Constructor");
   }
}
