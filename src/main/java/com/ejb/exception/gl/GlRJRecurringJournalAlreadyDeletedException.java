package com.ejb.exception.gl;

import com.util.Debug;

public class GlRJRecurringJournalAlreadyDeletedException extends Exception {

   public GlRJRecurringJournalAlreadyDeletedException() {
      Debug.print("GlRJRecurringJournalAlreadyDeletedException Constructor");
   }

   public GlRJRecurringJournalAlreadyDeletedException(String msg) {
      super(msg);
      Debug.print("GlRJRecurringJournalAlreadyDeletedException Constructor");
   }
}
