package com.ejb.exception.gl;

import com.util.Debug;

public class GlRJLRecurringJournalLineAlreadyExistException extends Exception {

   public GlRJLRecurringJournalLineAlreadyExistException() {
      Debug.print("GlRJLRecurringJournalLineAlreadyExistException Constructor");
   }

   public GlRJLRecurringJournalLineAlreadyExistException(String msg) {
      super(msg);
      Debug.print("GlRJLRecurringJournalLineAlreadyExistException Constructor");
   }
}
