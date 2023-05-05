package com.ejb.exception.gl;

import com.util.Debug;

public class GlJRVNoPostedJournalBatchFoundException extends Exception {

   public GlJRVNoPostedJournalBatchFoundException() { 
      Debug.print("GlJRVNoPostedJournalBatchFoundException Constructor");
   }

   public GlJRVNoPostedJournalBatchFoundException(String msg) {
      super(msg);
      Debug.print("GlJRVNoPostedJournalBatchFoundException Constructor");
   }
}
