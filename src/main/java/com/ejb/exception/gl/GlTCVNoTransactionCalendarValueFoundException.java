package com.ejb.exception.gl;

import com.util.Debug;

public class GlTCVNoTransactionCalendarValueFoundException extends Exception {

   public GlTCVNoTransactionCalendarValueFoundException() {
      Debug.print("GlTCVNoTransactionCalendarValueFoundException Constructor");
   }

   public GlTCVNoTransactionCalendarValueFoundException(String msg) {
      super(msg);
      Debug.print("GlTCVNoTransactionCalendarValueFoundException Constructor");
   }
}
