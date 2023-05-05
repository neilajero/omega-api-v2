package com.ejb.exception.ad;

import com.util.Debug;

public class AdPRFCoaInvPreparedFoodTaxAccountNotFoundException extends Exception {

   public AdPRFCoaInvPreparedFoodTaxAccountNotFoundException() {
      Debug.print("AdPRFCoaInvPreparedFoodTaxAccountNotFoundException Constructor");
   }

   public AdPRFCoaInvPreparedFoodTaxAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("AdPRFCoaInvPreparedFoodTaxAccountNotFoundException Constructor");
   }
}
