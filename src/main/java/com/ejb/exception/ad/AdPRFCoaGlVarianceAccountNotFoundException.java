package com.ejb.exception.ad;

import com.util.Debug;

public class AdPRFCoaGlVarianceAccountNotFoundException extends Exception {

   public AdPRFCoaGlVarianceAccountNotFoundException() {
      Debug.print("AdPRFCoaGlVarianceAccountNotFoundException Constructor");
   }

   public AdPRFCoaGlVarianceAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("AdPRFCoaGlVarianceAccountNotFoundException Constructor");
   }
}
