package com.ejb.exception.gl;

import com.util.Debug;

public class GlTCTransactionCalendarAlreadyDeletedException extends Exception {

   public GlTCTransactionCalendarAlreadyDeletedException() {
      Debug.print("GlTCTransactionCalendarAlreadyDeletedException Constructor");
   }

   public GlTCTransactionCalendarAlreadyDeletedException(String msg) {
      super(msg);
      Debug.print("GlTCTransactionCalendarAlreadyDeletedException Constructor");
   }
}
