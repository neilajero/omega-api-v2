package com.ejb.exception.gl;

import com.util.Debug;

public class GlSOBAccountingCalendarAlreadyAssignedException extends Exception {

   public GlSOBAccountingCalendarAlreadyAssignedException() {
      Debug.print("GlSOBAccountingCalendarAlreadyAssignedException Constructor");
   }

   public GlSOBAccountingCalendarAlreadyAssignedException(String msg) {
      super(msg);
      Debug.print("GlSOBAccountingCalendarAlreadyAssignedException Constructor");
   }
}
