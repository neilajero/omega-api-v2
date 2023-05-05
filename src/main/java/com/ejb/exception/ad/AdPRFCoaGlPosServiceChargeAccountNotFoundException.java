package com.ejb.exception.ad;

import com.util.Debug;

public class AdPRFCoaGlPosServiceChargeAccountNotFoundException extends Exception {

   public AdPRFCoaGlPosServiceChargeAccountNotFoundException() {
      Debug.print("AdPRFCoaGlPosServiceChargeAccountNotFoundException Constructor");
   }

   public AdPRFCoaGlPosServiceChargeAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("AdPRFCoaGlPosServiceChargeAccountNotFoundException Constructor");
   }
}
