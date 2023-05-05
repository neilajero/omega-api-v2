package com.ejb.exception.gl;

import com.util.Debug;

public class GlACVNotTotalToOneYearException extends Exception {

   public GlACVNotTotalToOneYearException() {
      Debug.print("GlACVNotTotalToOneYearException Constructor");
   }

   public GlACVNotTotalToOneYearException(String msg) {
      super(msg);
      Debug.print("GlACVNotTotalToOneYearException Constructor");
   }
}
