package com.ejb.exception.gl;

import com.util.Debug;

public class GlFCFunctionalCurrencyAlreadyExistException extends Exception {

   public GlFCFunctionalCurrencyAlreadyExistException() {
      Debug.print("GlFCFunctionalCurrencyAlreadyExistException Constructor");
   }

   public GlFCFunctionalCurrencyAlreadyExistException(String msg) {
      super(msg);
      Debug.print("GlFCFunctionalCurrencyAlreadyExistException Constructor");
   }
}
