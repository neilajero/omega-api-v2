package com.ejb.exception.ad;

import com.util.Debug;

public class AdBACoaGlAdvanceAccountNotFoundException extends Exception {

   public AdBACoaGlAdvanceAccountNotFoundException() {
      Debug.print("AdBACoaGlAdvanceAccountNotFoundException Constructor");
   }

   public AdBACoaGlAdvanceAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("AdBACoaGlAdvanceAccountNotFoundException Constructor");
   }
}
