package com.ejb.exception.gl;

import com.util.Debug;

public class GlJCNoJournalCategoryFoundException extends Exception {

   public GlJCNoJournalCategoryFoundException() {
      Debug.print("GlJCNoJournalCategoryFoundException Constructor");
   }

   public GlJCNoJournalCategoryFoundException(String msg) {
      super(msg);
      Debug.print("GlJCNoJournalCategoryFoundException Constructor");
   }
}
