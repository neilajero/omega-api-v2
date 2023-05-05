package com.ejb.exception.gl;

import com.util.Debug;

public class GlSOBTransactionCalendarAlreadyAssignedException extends Exception {

   public GlSOBTransactionCalendarAlreadyAssignedException() {
      Debug.print("GlSOBTransactionCalendarAlreadyAssignedException Constructor");
   }

   public GlSOBTransactionCalendarAlreadyAssignedException(String msg) {
      super(msg);
      Debug.print("GlSOBTransactionCalendarAlreadyAssignedException Constructor");
   }
}
