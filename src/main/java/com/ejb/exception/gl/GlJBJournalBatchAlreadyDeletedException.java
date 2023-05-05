package com.ejb.exception.gl;

import com.util.Debug;

public class GlJBJournalBatchAlreadyDeletedException extends Exception {

   public GlJBJournalBatchAlreadyDeletedException() {
      Debug.print("GlJBJournalBatchAlreadyDeletedException Constructor");
   }

   public GlJBJournalBatchAlreadyDeletedException(String msg) {
      super(msg);
      Debug.print("GlJBJournalBatchAlreadyDeletedException Constructor");
   }
}
