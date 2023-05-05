package com.ejb.exception.gl;

import com.util.Debug;

public class GlRepGLEnteredDateRangeHasOpenPeriodException extends Exception {

   public GlRepGLEnteredDateRangeHasOpenPeriodException() {
      Debug.print("GlRepGLEnteredDateRangeHasOpenPeriodException Constructor");
   }

   public GlRepGLEnteredDateRangeHasOpenPeriodException(String msg) {
      super(msg);
      Debug.print("GlRepGLEnteredDateRangeHasOpenPeriodException Constructor");
   }
}
