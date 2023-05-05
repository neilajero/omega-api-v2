package com.ejb.exception.gl;

import com.util.Debug;

public class GlACNoAccountingCalendarFoundException extends Exception {

   public GlACNoAccountingCalendarFoundException() {
      Debug.print("GlACNoAccountingCalendarFoundException Constructor");
   }

   public GlACNoAccountingCalendarFoundException(String msg) {
      super(msg);
      Debug.print("GlACNoAccountingCalendarFoundException Constructor");
   }
}
