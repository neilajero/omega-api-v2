package com.ejb.exception.gl;

import com.util.Debug;

public class GlACAlreadyHasAccountingCalendarValueException extends Exception {

   public GlACAlreadyHasAccountingCalendarValueException() {
      Debug.print("GlACAlreadyHasAccountingCalendarValueException Constructor");
   }

   public GlACAlreadyHasAccountingCalendarValueException(String msg) {
      super(msg);
      Debug.print("GlACAlreadyHasAccountingCalendarValueException Constructor");
   }
}
