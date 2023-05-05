package com.ejb.exception.gl;

import com.util.Debug;

public class GlRJSRecurringJournalSchedulerAlreadyExistException extends Exception {

   public GlRJSRecurringJournalSchedulerAlreadyExistException() {
      Debug.print("GlRJSRecurringJournalSchedulerAlreadyExistException Constructor");
   }

   public GlRJSRecurringJournalSchedulerAlreadyExistException(String msg) {
      super(msg);
      Debug.print("GlRJSRecurringJournalSchedulerAlreadyExistException Constructor");
   }
}
