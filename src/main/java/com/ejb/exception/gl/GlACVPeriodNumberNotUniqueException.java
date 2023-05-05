package com.ejb.exception.gl;

import com.util.Debug;

public class GlACVPeriodNumberNotUniqueException extends Exception {

   public GlACVPeriodNumberNotUniqueException() {
      Debug.print("GlACVPeriodNumberNotUniqueException Constructor");
   }

   public GlACVPeriodNumberNotUniqueException(String msg) {
      super(msg);
      Debug.print("GlACVPeriodNumberNotUniqueException Constructor");
   }
}
