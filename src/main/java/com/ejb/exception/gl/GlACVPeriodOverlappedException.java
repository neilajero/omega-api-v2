package com.ejb.exception.gl;

import com.util.Debug;

public class GlACVPeriodOverlappedException extends Exception {

   public GlACVPeriodOverlappedException() {
      Debug.print("GlACVPeriodOverlappedException Constructor");
   }

   public GlACVPeriodOverlappedException(String msg) {
      super(msg);
      Debug.print("GlACVPeriodOverlappedException Constructor");
   }
}
