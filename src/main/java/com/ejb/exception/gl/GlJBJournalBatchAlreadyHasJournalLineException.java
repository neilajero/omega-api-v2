package com.ejb.exception.gl;

import com.util.Debug;

public class GlJBJournalBatchAlreadyHasJournalLineException extends Exception {

   public GlJBJournalBatchAlreadyHasJournalLineException() {
      Debug.print("GlJBJournalBatchAlreadyHasJournalLineException Constructor");
   }

   public GlJBJournalBatchAlreadyHasJournalLineException(String msg) {
      super(msg);
      Debug.print("GlJBJournalBatchAlreadyHasJournalLineException Constructor");
   }
}
