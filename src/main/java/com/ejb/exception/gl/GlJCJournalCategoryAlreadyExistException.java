package com.ejb.exception.gl;

import com.util.Debug;

public class GlJCJournalCategoryAlreadyExistException extends Exception {

   public GlJCJournalCategoryAlreadyExistException() {
      Debug.print("GlJCJournalCategoryAlreadyExistException Constructor");
   }

   public GlJCJournalCategoryAlreadyExistException(String msg) {
      super(msg);
      Debug.print("GlJCJournalCategoryAlreadyExistException Constructor");
   }
}
