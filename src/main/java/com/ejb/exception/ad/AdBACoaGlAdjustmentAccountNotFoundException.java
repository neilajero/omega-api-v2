package com.ejb.exception.ad;

import com.util.Debug;

public class AdBACoaGlAdjustmentAccountNotFoundException extends Exception {

   public AdBACoaGlAdjustmentAccountNotFoundException() {
      Debug.print("AdBACoaGlAdjustmentAccountNotFoundException Constructor");
   }

   public AdBACoaGlAdjustmentAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("AdBACoaGlAdjustmentAccountNotFoundException Constructor");
   }
}
