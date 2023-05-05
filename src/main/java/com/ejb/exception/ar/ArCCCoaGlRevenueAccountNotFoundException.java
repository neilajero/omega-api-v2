package com.ejb.exception.ar;

import com.util.Debug;

public class ArCCCoaGlRevenueAccountNotFoundException extends Exception {

   public ArCCCoaGlRevenueAccountNotFoundException() {
      Debug.print("ArCCCoaGlRevenueAccountNotFoundException Constructor");
   }

   public ArCCCoaGlRevenueAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("ArCCCoaGlRevenueAccountNotFoundException Constructor");
   }
}
