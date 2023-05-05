package com.ejb.exception.gl;

import com.util.Debug;

public class GlRJSRecurringJournalSchedulerAlreadyDeletedException extends Exception {

   public GlRJSRecurringJournalSchedulerAlreadyDeletedException() {
      Debug.print("GlRJSRecurringJournalSchedulerAlreadyDeletedException Constructor");
   }

   public GlRJSRecurringJournalSchedulerAlreadyDeletedException(String msg) {
      super(msg);
      Debug.print("GlRJSRecurringJournalSchedulerAlreadyDeletedException Constructor");
   }
}
