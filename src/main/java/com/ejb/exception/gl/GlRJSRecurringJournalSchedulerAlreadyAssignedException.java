package com.ejb.exception.gl;

import com.util.Debug;

public class GlRJSRecurringJournalSchedulerAlreadyAssignedException extends Exception {

   public GlRJSRecurringJournalSchedulerAlreadyAssignedException() {
      Debug.print("GlRJSRecurringJournalSchedulerAlreadyAssignedException Constructor");
   }

   public GlRJSRecurringJournalSchedulerAlreadyAssignedException(String msg) {
      super(msg);
      Debug.print("GlRJSRecurringJournalSchedulerAlreadyAssignedException Constructor");
   }
}
