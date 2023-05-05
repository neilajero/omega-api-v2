package com.ejb.exception.ar;

import com.util.Debug;

public class ArCCCoaGlEarnedPenaltyAccountNotFoundException extends Exception {

   public ArCCCoaGlEarnedPenaltyAccountNotFoundException() {
      Debug.print("ArCCCoaGlEarnedPenaltyAccountNotFoundException Constructor");
   }

   public ArCCCoaGlEarnedPenaltyAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("ArCCCoaGlEarnedPenaltyAccountNotFoundException Constructor");
   }
}
