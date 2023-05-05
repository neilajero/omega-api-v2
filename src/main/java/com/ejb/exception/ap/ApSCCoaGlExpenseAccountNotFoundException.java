package com.ejb.exception.ap;

import com.util.Debug;

public class ApSCCoaGlExpenseAccountNotFoundException extends Exception {

   public ApSCCoaGlExpenseAccountNotFoundException() {
      Debug.print("ApSCCoaGlExpenseAccountNotFoundException Constructor");
   }

   public ApSCCoaGlExpenseAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("ApSCCoaGlExpenseAccountNotFoundException Constructor");
   }
}
