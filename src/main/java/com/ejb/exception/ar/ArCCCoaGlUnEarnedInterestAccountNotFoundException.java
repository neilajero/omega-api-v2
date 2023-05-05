package com.ejb.exception.ar;

import com.util.Debug;

public class ArCCCoaGlUnEarnedInterestAccountNotFoundException extends Exception {

   public ArCCCoaGlUnEarnedInterestAccountNotFoundException() {
      Debug.print("ArCCCoaGlUnEarnedInterestAccountNotFoundException Constructor");
   }

   public ArCCCoaGlUnEarnedInterestAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("ArCCCoaGlUnEarnedInterestAccountNotFoundException Constructor");
   }
}
