package com.ejb.exception.ar;

import com.util.Debug;

public class ArJOTCoaGlJobOrderAccountNotFoundException extends Exception {

   public ArJOTCoaGlJobOrderAccountNotFoundException() {
      Debug.print("ArJOTCoaGlJobOrderAccountNotFoundException Constructor");
   }

   public ArJOTCoaGlJobOrderAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("ArJOTCoaGlJobOrderAccountNotFoundException Constructor");
   }
}
