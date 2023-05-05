package com.ejb.exception.gl;

import com.util.Debug;

public class GlACAccountingCalendarAlreadyDeletedException extends Exception {

   public GlACAccountingCalendarAlreadyDeletedException() { 
      Debug.print("GlACAccountingCalendarAlreadyDeletedException Constructor");
   }

   public GlACAccountingCalendarAlreadyDeletedException(String msg) {
      super(msg);
      Debug.print("GlACAccountingCalendarAlreadyDeletedException Constructor");
   }
}
