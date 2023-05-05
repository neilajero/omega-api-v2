package com.ejb.exception.gl;

import com.util.Debug;

public class GlACAccountingCalendarAlreadyExistException extends Exception {

   public GlACAccountingCalendarAlreadyExistException() {
      Debug.print("GlACAccountingCalendarAlreadyExistException Constructor");
   }

   public GlACAccountingCalendarAlreadyExistException(String msg) {
      super(msg);
      Debug.print("GlACAccountingCalendarAlreadyExistException Constructor");
   }
}
