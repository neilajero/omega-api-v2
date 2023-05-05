package com.ejb.exception.gl;

import com.util.Debug;

public class GlJPNoJournalBatchFoundException extends Exception {

   public GlJPNoJournalBatchFoundException() {
      Debug.print("GlJPNoJournalBatchFoundException Constructor");
   }

   public GlJPNoJournalBatchFoundException(String msg) {
      super(msg);
      Debug.print("GlJPNoJournalBatchFoundException Constructor");
   }
}
