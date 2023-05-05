package com.ejb.exception.gl;

import com.util.Debug;

public class GlSOBAccountingCalendarOpenException extends Exception {

   public GlSOBAccountingCalendarOpenException() {
      Debug.print("GlSOBAccountingCalendarOpenException Constructor");
   }

   public GlSOBAccountingCalendarOpenException(String msg) {
      super(msg);
      Debug.print("GlSOBAccountingCalendarOpenException Constructor");
   }
}
