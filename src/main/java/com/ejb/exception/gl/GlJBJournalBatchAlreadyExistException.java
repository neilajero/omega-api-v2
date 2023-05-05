package com.ejb.exception.gl;

import com.util.Debug;

public class GlJBJournalBatchAlreadyExistException extends Exception {

   public GlJBJournalBatchAlreadyExistException() {
      Debug.print("GlJBJournalBatchAlreadyExistException Constructor");
   }

   public GlJBJournalBatchAlreadyExistException(String msg) {
      super(msg);
      Debug.print("GlJBJournalBatchAlreadyExistException Constructor");
   }
}
