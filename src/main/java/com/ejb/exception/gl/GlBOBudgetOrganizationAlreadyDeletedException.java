package com.ejb.exception.gl;

import com.util.Debug;

public class GlBOBudgetOrganizationAlreadyDeletedException extends Exception {

   public GlBOBudgetOrganizationAlreadyDeletedException() {
      Debug.print("GlBOBudgetOrganizationAlreadyDeletedException Constructor");
   }

   public GlBOBudgetOrganizationAlreadyDeletedException(String msg) {
      super(msg);
      Debug.print("GlBOBudgetOrganizationAlreadyDeletedException Constructor");
   }
}
