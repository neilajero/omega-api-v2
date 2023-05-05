package com.ejb.exception.gl;

import com.util.Debug;

public class GlACVNoAccountingCalendarValueFoundException extends Exception {

   public GlACVNoAccountingCalendarValueFoundException() {
      Debug.print("GlACVNoAccountingCalendarValueFoundException Constructor");
   }

   public GlACVNoAccountingCalendarValueFoundException(String msg) {
      super(msg);
      Debug.print("GlACVNoAccountingCalendarValueFoundException Constructor");
   }
}
