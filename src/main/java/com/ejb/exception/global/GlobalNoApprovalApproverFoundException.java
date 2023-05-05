package com.ejb.exception.global;

import com.util.Debug;

public class GlobalNoApprovalApproverFoundException extends Exception {

   public GlobalNoApprovalApproverFoundException() {
      Debug.print("GlobalNoApprovalApproverFoundException Constructor");
   }

   public GlobalNoApprovalApproverFoundException(String msg) {
      super(msg);
      Debug.print("GlobalNoApprovalApproverFoundException Constructor");
   }
}
