package com.ejb.exception.gl;

import com.util.Debug;

public class GlRJBRecurringJournalBatchAlreadyAssignedException extends Exception {

   public GlRJBRecurringJournalBatchAlreadyAssignedException() {
      Debug.print("GlRJBRecurringJournalBatchAlreadyAssignedException Constructor");
   }

   public GlRJBRecurringJournalBatchAlreadyAssignedException(String msg) {
      super(msg);
      Debug.print("GlRJBRecurringJournalBatchAlreadyAssignedException Constructor");
   }
}
