package com.ejb.exception.gl;

import com.util.Debug;

public class GlRJLRecurringJournalLineAlreadyDeletedException extends Exception {

   public GlRJLRecurringJournalLineAlreadyDeletedException() {
      Debug.print("GlRJLRecurringJournalLineAlreadyDeletedException Constructor");
   }

   public GlRJLRecurringJournalLineAlreadyDeletedException(String msg) {
      super(msg);
      Debug.print("GlRJLRecurringJournalLineAlreadyDeletedException Constructor");
   }
}
