package com.ejb.exception.gl;

import com.util.Debug;

public class GlFCFunctionalCurrencyAlreadyAssignedException extends Exception {

   public GlFCFunctionalCurrencyAlreadyAssignedException() {
      Debug.print("GlFCFunctionalCurrencyAlreadyAssignedException Constructor");
   }

   public GlFCFunctionalCurrencyAlreadyAssignedException(String msg) {
      super(msg);
      Debug.print("GlFCFunctionalCurrencyAlreadyAssignedException Constructor");
   }
}
