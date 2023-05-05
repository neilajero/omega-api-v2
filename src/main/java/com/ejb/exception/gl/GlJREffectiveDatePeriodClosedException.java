package com.ejb.exception.gl;

import com.util.Debug;

public class GlJREffectiveDatePeriodClosedException extends Exception {

   public GlJREffectiveDatePeriodClosedException() {
      Debug.print("GlJREffectiveDatePeriodClosedException Constructor");
   }

   public GlJREffectiveDatePeriodClosedException(String msg) {
      super(msg);
      Debug.print("GlJREffectiveDatePeriodClosedException Constructor");
   }
}
