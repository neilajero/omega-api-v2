package com.ejb.exception.ad;

import com.util.Debug;

public class AdPRFCoaGlPOSAdjustmentAccountNotFoundException extends Exception {

   public AdPRFCoaGlPOSAdjustmentAccountNotFoundException() {
      Debug.print("AdPRFCoaGlPOSAdjustmentAccountNotFoundException Constructor");
   }

   public AdPRFCoaGlPOSAdjustmentAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("AdPRFCoaGlPOSAdjustmentAccountNotFoundException Constructor");
   }
}
