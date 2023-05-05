package com.ejb.exception.gl;

import com.util.Debug;

public class GlRJBRecurringJournalBatchAlreadyExistException extends Exception {

   public GlRJBRecurringJournalBatchAlreadyExistException() {
      Debug.print("GlRJBRecurringJournalBatchAlreadyExistException Constructor");
   }

   public GlRJBRecurringJournalBatchAlreadyExistException(String msg) {
      super(msg);
      Debug.print("GlRJBRecurringJournalBatchAlreadyExistException Constructor");
   }
}
