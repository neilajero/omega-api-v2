package com.ejb.exception.gl;

import com.util.Debug;

public class GlTCVTransactionCalendarValueAlreadyDeletedException extends Exception {

   public GlTCVTransactionCalendarValueAlreadyDeletedException() {
      Debug.print("GlTCVTransactionCalendarValueAlreadyDeletedException Constructor");
   }

   public GlTCVTransactionCalendarValueAlreadyDeletedException(String msg) {
      super(msg);
      Debug.print("GlTCVTransactionCalendarValueAlreadyDeletedException Constructor");
   }
}
