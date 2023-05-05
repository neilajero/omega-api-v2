package com.ejb.exception.gl;

import com.util.Debug;

public class GlACPeriodTypeNotFoundException extends Exception {

   public GlACPeriodTypeNotFoundException() { 
      Debug.print("GlACPeriodTypeNotFoundException Constructor");
   }

   public GlACPeriodTypeNotFoundException(String msg) {
      super(msg);
      Debug.print("GlACPeriodTypeNotFoundException Constructor");
   }
}
