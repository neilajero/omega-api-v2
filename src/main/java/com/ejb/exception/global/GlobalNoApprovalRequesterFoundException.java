package com.ejb.exception.global;

import com.util.Debug;

public class GlobalNoApprovalRequesterFoundException extends Exception {

   public GlobalNoApprovalRequesterFoundException() {
      Debug.print("GlobalNoApprovalRequesterFoundException Constructor");
   }

   public GlobalNoApprovalRequesterFoundException(String msg) {
      super(msg);
      Debug.print("GlobalNoApprovalRequesterFoundException Constructor");
   }
}
