package com.ejb.exception.gl;

import com.util.Debug;

public class GlRJRecurringJournalAlreadyExistException extends Exception {

   public GlRJRecurringJournalAlreadyExistException() {
      Debug.print("GlRJRecurringJournalAlreadyExistException Constructor");
   }

   public GlRJRecurringJournalAlreadyExistException(String msg) {
      super(msg);
      Debug.print("GlRJRecurringJournalAlreadyExistException Constructor");
   }
}
