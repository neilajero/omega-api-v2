package com.ejb.exception.gl;

import com.util.Debug;

public class GlTCNoTransactionCalendarFoundException extends Exception {

   public GlTCNoTransactionCalendarFoundException() {
      Debug.print("GlTCNoTransactionCalendarFoundException Constructor");
   }

   public GlTCNoTransactionCalendarFoundException(String msg) {
      super(msg);
      Debug.print("GlTCNoTransactionCalendarFoundException Constructor");
   }
}
