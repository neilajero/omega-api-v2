package com.ejb.exception.ad;

import com.util.Debug;

public class AdBACoaGlBankChargeAccountNotFoundException extends Exception {

   public AdBACoaGlBankChargeAccountNotFoundException() {
      Debug.print("AdBACoaGlBankChargeAccountNotFoundException Constructor");
   }

   public AdBACoaGlBankChargeAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("AdBACoaGlBankChargeAccountNotFoundException Constructor");
   }
}
