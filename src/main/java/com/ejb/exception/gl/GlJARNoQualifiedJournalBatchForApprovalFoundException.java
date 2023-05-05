package com.ejb.exception.gl;

import com.util.Debug;

public class GlJARNoQualifiedJournalBatchForApprovalFoundException extends Exception {

   public GlJARNoQualifiedJournalBatchForApprovalFoundException() {
      Debug.print("GlJARNoQualifiedJournalBatchForApprovalFoundException Constructor");
   }

   public GlJARNoQualifiedJournalBatchForApprovalFoundException(String msg) {
      super(msg);
      Debug.print("GlJARNoQualifiedJournalBatchForApprovalFoundException Constructor");
   }
}
