package com.ejb.exception.ap;

import com.util.Debug;

public class ApSCCoaGlPayableAccountNotFoundException extends Exception {

   public ApSCCoaGlPayableAccountNotFoundException() {
      Debug.print("ApSCCoaGlPayableAccountNotFoundException Constructor");
   }

   public ApSCCoaGlPayableAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("ApSCCoaGlPayableAccountNotFoundException Constructor");
   }
}
