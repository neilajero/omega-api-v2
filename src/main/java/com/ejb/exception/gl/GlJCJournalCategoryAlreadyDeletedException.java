package com.ejb.exception.gl;

import com.util.Debug;

public class GlJCJournalCategoryAlreadyDeletedException  extends Exception {

   public GlJCJournalCategoryAlreadyDeletedException () {
      Debug.print("GlJCJournalCategoryAlreadyDeletedException  Constructor");
   }

   public GlJCJournalCategoryAlreadyDeletedException (String msg) {
      super(msg);
      Debug.print("GlJCJournalCategoryAlreadyDeletedException  Constructor");
   }
}
