package com.ejb.exception.ad;

import com.util.Debug;

public class AdPRFCoaInvSalesTaxAccountNotFoundException extends Exception {

   public AdPRFCoaInvSalesTaxAccountNotFoundException() {
      Debug.print("AdPRFCoaInvSalesTaxAccountNotFoundException Constructor");
   }

   public AdPRFCoaInvSalesTaxAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("AdPRFCoaInvSalesTaxAccountNotFoundException Constructor");
   }
}
