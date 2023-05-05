package com.ejb.exception.gl;

import com.util.Debug;

public class GlACNoAccountingCalendarClosedOrPermanentyClosedFoundException extends Exception {

   public GlACNoAccountingCalendarClosedOrPermanentyClosedFoundException() {
      Debug.print("GlACNoAccountingCalendarClosedOrPermanentyClosedFoundException Constructor");
   }

   public GlACNoAccountingCalendarClosedOrPermanentyClosedFoundException(String msg) {
      super(msg);
      Debug.print("GlACNoAccountingCalendarClosedOrPermanentyClosedFoundException Constructor");
   }
}
