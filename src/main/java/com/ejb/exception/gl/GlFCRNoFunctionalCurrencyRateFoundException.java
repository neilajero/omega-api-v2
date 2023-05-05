package com.ejb.exception.gl;

import com.util.Debug;

public class GlFCRNoFunctionalCurrencyRateFoundException extends Exception {

   public GlFCRNoFunctionalCurrencyRateFoundException() {
      Debug.print("GlFCRNoFunctionalCurrencyRateFoundException Constructor");
   }

   public GlFCRNoFunctionalCurrencyRateFoundException(String msg) {
      super(msg);
      Debug.print("GlFCRNoFunctionalCurrencyRateFoundException Constructor");
   }
}
