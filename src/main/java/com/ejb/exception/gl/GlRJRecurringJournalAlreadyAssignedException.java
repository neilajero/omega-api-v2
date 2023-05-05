package com.ejb.exception.gl;

import com.util.Debug;

public class GlRJRecurringJournalAlreadyAssignedException extends Exception {

   public GlRJRecurringJournalAlreadyAssignedException() {
      Debug.print("GlRJRecurringJournalAlreadyAssignedException Constructor");
   }

   public GlRJRecurringJournalAlreadyAssignedException(String msg) {
      super(msg);
      Debug.print("GlRJRecurringJournalAlreadyAssignedException Constructor");
   }
}
