package com.ejb.exception.ar;

import com.util.Debug;

public class ArCCCoaGlEarnedInterestAccountNotFoundException extends Exception {

   public ArCCCoaGlEarnedInterestAccountNotFoundException() {
      Debug.print("ArCCCoaGlEarnedInterestAccountNotFoundException Constructor");
   }

   public ArCCCoaGlEarnedInterestAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("ArCCCoaGlEarnedInterestAccountNotFoundException Constructor");
   }
}
