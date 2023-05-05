package com.ejb.exception.gl;

import com.util.Debug;

public class GlJRJournalAlreadyReversedException extends Exception {

   public GlJRJournalAlreadyReversedException() {
      Debug.print("GlJRJournalAlreadyReversedException Constructor");
   }

   public GlJRJournalAlreadyReversedException(String msg) {
      super(msg);
      Debug.print("GlJRJournalAlreadyReversedException Constructor");
   }
}
