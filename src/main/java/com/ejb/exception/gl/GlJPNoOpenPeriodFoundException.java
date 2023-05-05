package com.ejb.exception.gl;

import com.util.Debug;

public class GlJPNoOpenPeriodFoundException extends Exception {

   public GlJPNoOpenPeriodFoundException() {
      Debug.print("GlJPNoOpenPeriodFoundException Constructor");
   }

   public GlJPNoOpenPeriodFoundException(String msg) {
      super(msg);
      Debug.print("GlJPNoOpenPeriodFoundException Constructor");
   }
}
