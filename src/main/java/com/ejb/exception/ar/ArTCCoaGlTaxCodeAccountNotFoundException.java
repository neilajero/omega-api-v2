package com.ejb.exception.ar;

import com.util.Debug;

public class ArTCCoaGlTaxCodeAccountNotFoundException extends Exception {

   public ArTCCoaGlTaxCodeAccountNotFoundException() {
      Debug.print("ArTCCoaGlTaxCodeAccountNotFoundException Constructor");
   }

   public ArTCCoaGlTaxCodeAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("ArTCCoaGlTaxCodeAccountNotFoundException Constructor");
   }
}
