package com.ejb.exception.ad;

import com.util.Debug;

public class AdBACoaGlUnappliedCheckNotFoundException extends Exception {

   public AdBACoaGlUnappliedCheckNotFoundException() {
      Debug.print("AdBACoaGlUnappliedCheckNotFoundException Constructor");
   }

   public AdBACoaGlUnappliedCheckNotFoundException(String msg) {
      super(msg);
      Debug.print("AdBACoaGlUnappliedCheckNotFoundException Constructor");
   }
}
