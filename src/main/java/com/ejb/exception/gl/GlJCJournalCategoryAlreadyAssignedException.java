package com.ejb.exception.gl;

import com.util.Debug;

public class GlJCJournalCategoryAlreadyAssignedException extends Exception {

   public GlJCJournalCategoryAlreadyAssignedException() {
      Debug.print("GlJCJournalCategoryAlreadyAssignedException Constructor");
   }

   public GlJCJournalCategoryAlreadyAssignedException(String msg) {
      super(msg);
      Debug.print("GlJCJournalCategoryAlreadyAssignedException Constructor");
   }
}
