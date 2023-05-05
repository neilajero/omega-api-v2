package com.ejb.exception.gl;

import com.util.Debug;

public class GlPTNoPeriodTypeFoundException extends Exception {

   public GlPTNoPeriodTypeFoundException() {
      Debug.print("GlPTNoPeriodTypeFoundException Constructor");
   }

   public GlPTNoPeriodTypeFoundException(String msg) {
      super(msg);
      Debug.print("GlPTNoPeriodTypeFoundException Constructor");
   }
}
