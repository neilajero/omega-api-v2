package com.ejb.exception.gl;

import com.util.Debug;

public class GlTCTransactionCalendarAlreadyAssignedException extends Exception {

   public GlTCTransactionCalendarAlreadyAssignedException() {
      Debug.print("GlTCTransactionCalendarAlreadyAssignedException Constructor");
   }

   public GlTCTransactionCalendarAlreadyAssignedException(String msg) {
      super(msg);
      Debug.print("GlTCTransactionCalendarAlreadyAssignedException Constructor");
   }
}
