package com.ejb.exception.gl;

import com.util.Debug;

public class GlRJBNoRecurringJournalBatchFoundException extends Exception {

   public GlRJBNoRecurringJournalBatchFoundException() {
      Debug.print("GlRJBNoRecurringJournalBatchFoundException Constructor");
   }

   public GlRJBNoRecurringJournalBatchFoundException(String msg) {
      super(msg);
      Debug.print("GlRJBNoRecurringJournalBatchFoundException Constructor");
   }
}
