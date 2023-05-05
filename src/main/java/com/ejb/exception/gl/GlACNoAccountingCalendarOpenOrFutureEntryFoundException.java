package com.ejb.exception.gl;

import com.util.Debug;

public class GlACNoAccountingCalendarOpenOrFutureEntryFoundException extends Exception {

   public GlACNoAccountingCalendarOpenOrFutureEntryFoundException() {
      Debug.print("GlACNoAccountingCalendarOpenOrFutureEntryFoundException Constructor");
   }

   public GlACNoAccountingCalendarOpenOrFutureEntryFoundException(String msg) {
      super(msg);
      Debug.print("GlACNoAccountingCalendarOpenOrFutureEntryFoundException Constructor");
   }
}
