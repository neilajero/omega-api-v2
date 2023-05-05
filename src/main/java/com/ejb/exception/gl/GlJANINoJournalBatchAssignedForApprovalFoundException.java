package com.ejb.exception.gl;

import com.util.Debug;

public class GlJANINoJournalBatchAssignedForApprovalFoundException extends Exception {

   public GlJANINoJournalBatchAssignedForApprovalFoundException() {
      Debug.print("GlJANINoJournalBatchAssignedForApprovalFoundException Constructor");
   }

   public GlJANINoJournalBatchAssignedForApprovalFoundException(String msg) {
      super(msg);
      Debug.print("GlJANINoJournalBatchAssignedForApprovalFoundException Constructor");
   }
}
