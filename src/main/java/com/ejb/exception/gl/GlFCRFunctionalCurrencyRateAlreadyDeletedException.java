package com.ejb.exception.gl;

import com.util.Debug;

public class GlFCRFunctionalCurrencyRateAlreadyDeletedException extends Exception {

   public GlFCRFunctionalCurrencyRateAlreadyDeletedException() {
      Debug.print("GlFCRFunctionalCurrencyRateAlreadyDeletedException Constructor");
   }

   public GlFCRFunctionalCurrencyRateAlreadyDeletedException(String msg) {
      super(msg);
      Debug.print("GlFCRFunctionalCurrencyRateAlreadyDeletedException Constructor");
   }
}
