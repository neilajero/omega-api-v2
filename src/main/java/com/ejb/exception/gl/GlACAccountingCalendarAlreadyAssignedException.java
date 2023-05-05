package com.ejb.exception.gl;

import com.util.Debug;

public class GlACAccountingCalendarAlreadyAssignedException extends Exception {

   public GlACAccountingCalendarAlreadyAssignedException() {
      Debug.print("GlACAccountingCalendarAlreadyAssignedException Constructor");
   }

   public GlACAccountingCalendarAlreadyAssignedException(String msg) {
      super(msg);
      Debug.print("GlACAccountingCalendarAlreadyAssignedException Constructor");
   }
}
