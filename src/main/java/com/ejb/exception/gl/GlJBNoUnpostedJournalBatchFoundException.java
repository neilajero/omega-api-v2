package com.ejb.exception.gl;

import com.util.Debug;

public class GlJBNoUnpostedJournalBatchFoundException extends Exception {

   public GlJBNoUnpostedJournalBatchFoundException() {
      Debug.print("GlJBNoUnpostedJournalBatchFoundException Constructor");
   }

   public GlJBNoUnpostedJournalBatchFoundException(String msg) {
      super(msg);
      Debug.print("GlJBNoUnpostedJournalBatchFoundException Constructor");
   }
}
