package com.ejb.exception.gl;

import com.util.Debug;

public class GlBGTBudgetAlreadyDeletedException extends Exception {

   public GlBGTBudgetAlreadyDeletedException() {
      Debug.print("GlBGTBudgetAlreadyDeletedException Constructor");
   }

   public GlBGTBudgetAlreadyDeletedException(String msg) {
      super(msg);
      Debug.print("GlBGTBudgetAlreadyDeletedException Constructor");
   }
}
