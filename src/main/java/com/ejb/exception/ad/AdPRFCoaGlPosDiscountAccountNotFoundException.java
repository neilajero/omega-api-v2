package com.ejb.exception.ad;

import com.util.Debug;

public class AdPRFCoaGlPosDiscountAccountNotFoundException extends Exception {

   public AdPRFCoaGlPosDiscountAccountNotFoundException() {
      Debug.print("AdPRFCoaGlPosDiscountAccountNotFoundException Constructor");
   }

   public AdPRFCoaGlPosDiscountAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("AdPRFCoaGlPosDiscountAccountNotFoundException Constructor");
   }
}
