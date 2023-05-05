package com.ejb.exception.ar;

import com.util.Debug;

public class ArCCCoaGlReceivableAccountNotFoundException extends Exception {

   public ArCCCoaGlReceivableAccountNotFoundException() {
      Debug.print("ArCCCoaGlReceivableAccountNotFoundException Constructor");
   }

   public ArCCCoaGlReceivableAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("ArCCCoaGlReceivableAccountNotFoundException Constructor");
   }
}
