package com.ejb.exception.gl;

import com.util.Debug;

public class GlJRVNoUnreversedJournalFoundException extends Exception {

   public GlJRVNoUnreversedJournalFoundException() {
      Debug.print("GlJRVNoUnreversedJournalFoundException Constructor");
   }

   public GlJRVNoUnreversedJournalFoundException(String msg) {
      super(msg);
      Debug.print("GlJRVNoUnreversedJournalFoundException Constructor");
   }
}
