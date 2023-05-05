package com.ejb.exception.gl;

import com.util.Debug;

public class GlTCTransactionCalendarAlreadyExistException extends Exception {

   public GlTCTransactionCalendarAlreadyExistException() {
      Debug.print("GlTCTransactionCalendarAlreadyExistException Constructor");
   }

   public GlTCTransactionCalendarAlreadyExistException(String msg) {
      super(msg);
      Debug.print("GlTCTransactionCalendarAlreadyExistException Constructor");
   }
}
