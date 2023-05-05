package com.ejb.exception.gl;

import com.util.Debug;

public class GlRJLNoRecurringJournalLineFoundException extends Exception {

   public GlRJLNoRecurringJournalLineFoundException() {
      Debug.print("GlRJLNoRecurringJournalLineFoundException Constructor");
   }

   public GlRJLNoRecurringJournalLineFoundException(String msg) {
      super(msg);
      Debug.print("GlRJLNoRecurringJournalLineFoundException Constructor");
   }
}
