package com.ejb.exception.gl;

import com.util.Debug;

public class GlFCFunctionalCurrencyAlreadyDeletedException extends Exception {

   public GlFCFunctionalCurrencyAlreadyDeletedException() {
      Debug.print("GlFCFunctionalCurrencyAlreadyDeletedException Constructor");
   }

   public GlFCFunctionalCurrencyAlreadyDeletedException(String msg) {
      super(msg);
      Debug.print("GlFCFunctionalCurrencyAlreadyDeletedException Constructor");
   }
}
