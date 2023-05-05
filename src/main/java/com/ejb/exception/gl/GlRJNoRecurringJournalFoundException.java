package com.ejb.exception.gl;

import com.util.Debug;

public class GlRJNoRecurringJournalFoundException extends Exception {

   public GlRJNoRecurringJournalFoundException() {
      Debug.print("GlRJNoRecurringJournalFoundException Constructor");
   }

   public GlRJNoRecurringJournalFoundException(String msg) {
      super(msg);
      Debug.print("GlRJNoRecurringJournalFoundException Constructor");
   }
}
