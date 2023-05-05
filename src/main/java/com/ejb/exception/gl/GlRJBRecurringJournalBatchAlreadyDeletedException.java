package com.ejb.exception.gl;

import com.util.Debug;

public class GlRJBRecurringJournalBatchAlreadyDeletedException extends Exception {

   public GlRJBRecurringJournalBatchAlreadyDeletedException() {
      Debug.print("GlRJBRecurringJournalBatchAlreadyDeletedException Constructor");
   }

   public GlRJBRecurringJournalBatchAlreadyDeletedException(String msg) {
      super(msg);
      Debug.print("GlRJBRecurringJournalBatchAlreadyDeletedException Constructor");
   }
}
