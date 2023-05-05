package com.ejb.exception.gl;

import com.util.Debug;

public class GlSOBAccountingCalendarIsNotAllNeverOpenedException extends Exception {

   public GlSOBAccountingCalendarIsNotAllNeverOpenedException() {
      Debug.print("GlSOBAccountingCalendarIsNotAllNeverOpenedException Constructor");
   }

   public GlSOBAccountingCalendarIsNotAllNeverOpenedException(String msg) {
      super(msg);
      Debug.print("GlSOBAccountingCalendarIsNotAllNeverOpenedException Constructor");
   }
}
