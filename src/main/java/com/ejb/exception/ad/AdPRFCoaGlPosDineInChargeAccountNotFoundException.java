package com.ejb.exception.ad;

import com.util.Debug;

public class AdPRFCoaGlPosDineInChargeAccountNotFoundException extends Exception {

   public AdPRFCoaGlPosDineInChargeAccountNotFoundException() {
      Debug.print("AdPRFCoaGlPosDineInChargeAccountNotFoundException Constructor");
   }

   public AdPRFCoaGlPosDineInChargeAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("AdPRFCoaGlPosDineInChargeAccountNotFoundException Constructor");
   }
}
