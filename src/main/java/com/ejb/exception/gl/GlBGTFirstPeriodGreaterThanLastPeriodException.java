package com.ejb.exception.gl;

import com.util.Debug;

public class GlBGTFirstPeriodGreaterThanLastPeriodException extends Exception {

   public GlBGTFirstPeriodGreaterThanLastPeriodException() {
      Debug.print("GlBGTFirstPeriodGreaterThanLastPeriodException Constructor");
   }

   public GlBGTFirstPeriodGreaterThanLastPeriodException(String msg) {
      super(msg);
      Debug.print("GlBGTFirstPeriodGreaterThanLastPeriodException Constructor");
   }
}
