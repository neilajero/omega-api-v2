package com.ejb.exception.gl;

import com.util.Debug;

public class GlJBHasJournalWithFundStatusReservedException extends Exception {

   public GlJBHasJournalWithFundStatusReservedException() {
      Debug.print("GlJBHasJournalWithFundStatusReservedException Constructor");
   }

   public GlJBHasJournalWithFundStatusReservedException(String msg) {
      super(msg);
      Debug.print("GlJBHasJournalWithFundStatusReservedException Constructor");
   }
}
