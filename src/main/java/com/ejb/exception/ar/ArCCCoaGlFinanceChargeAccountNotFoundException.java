package com.ejb.exception.ar;

import com.util.Debug;

public class ArCCCoaGlFinanceChargeAccountNotFoundException extends Exception {

   public ArCCCoaGlFinanceChargeAccountNotFoundException() {
      Debug.print("ArCCCoaGlFinanceChargeAccountNotFoundException Constructor");
   }

   public ArCCCoaGlFinanceChargeAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("ArCCCoaGlFinanceChargeAccountNotFoundException Constructor");
   }
}
