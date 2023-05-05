package com.ejb.exception.gl;

import com.util.Debug;

public class GlRJSNoRecurringJournalSchedulerFoundException extends Exception {

   public GlRJSNoRecurringJournalSchedulerFoundException() {
      Debug.print("GlRJSNoRecurringJournalSchedulerFoundException Constructor");
   }

   public GlRJSNoRecurringJournalSchedulerFoundException(String msg) {
      super(msg);
      Debug.print("GlRJSNoRecurringJournalSchedulerFoundException Constructor");
   }
}
