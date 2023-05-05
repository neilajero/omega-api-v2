package com.ejb.exception.ad;

import com.util.Debug;

public class AdPRFCoaGlCustomerDepositAccountNotFoundException extends Exception {

   public AdPRFCoaGlCustomerDepositAccountNotFoundException() {
      Debug.print("AdPRFCoaGlCustomerDepositAccountNotFoundException Constructor");
   }

   public AdPRFCoaGlCustomerDepositAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("AdPRFCoaGlCustomerDepositAccountNotFoundException Constructor");
   }
}
