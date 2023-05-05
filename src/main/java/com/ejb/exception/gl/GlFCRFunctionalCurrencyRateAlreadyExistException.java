package com.ejb.exception.gl;

import com.util.Debug;

public class GlFCRFunctionalCurrencyRateAlreadyExistException extends Exception {

   public GlFCRFunctionalCurrencyRateAlreadyExistException() {
      Debug.print("GlFCRFunctionalCurrencyRateAlreadyExistException Constructor");
   }

   public GlFCRFunctionalCurrencyRateAlreadyExistException(String msg) {
      super(msg);
      Debug.print("GlFCRFunctionalCurrencyRateAlreadyExistException Constructor");
   }
}
