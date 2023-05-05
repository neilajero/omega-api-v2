package com.ejb.exception.gl;

import com.util.Debug;

public class GlFCNoFunctionalCurrencyFoundException extends Exception {

   public GlFCNoFunctionalCurrencyFoundException() {
      Debug.print("GlFCNoFunctionalCurrencyFoundException Constructor");
   }

   public GlFCNoFunctionalCurrencyFoundException(String msg) {
      super(msg);
      Debug.print("GlFCNoFunctionalCurrencyFoundException Constructor");
   }
}
