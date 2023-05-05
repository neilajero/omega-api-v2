package com.ejb.exception.gl;

import com.util.Debug;

public class GlJRNoFunctionalCurrencyFoundException extends Exception {

   public GlJRNoFunctionalCurrencyFoundException() {
      Debug.print("GlJRNoFunctionalCurrencyFoundException Constructor");
   }

   public GlJRNoFunctionalCurrencyFoundException(String msg) {
      super(msg);
      Debug.print("GlJRNoFunctionalCurrencyFoundException Constructor");
   }
}
