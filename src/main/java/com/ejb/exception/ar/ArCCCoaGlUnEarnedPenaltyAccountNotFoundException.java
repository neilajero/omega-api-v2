package com.ejb.exception.ar;

import com.util.Debug;

public class ArCCCoaGlUnEarnedPenaltyAccountNotFoundException extends Exception {

   public ArCCCoaGlUnEarnedPenaltyAccountNotFoundException() {
      Debug.print("ArCCCoaGlUnEarnedPenaltyAccountNotFoundException Constructor");
   }

   public ArCCCoaGlUnEarnedPenaltyAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("ArCCCoaGlUnEarnedPenaltyAccountNotFoundException Constructor");
   }
}
