package com.ejb.exception.global;

import com.util.Debug;

public class GlobalRecordInvalidForCurrentBranchException extends Exception {

   public GlobalRecordInvalidForCurrentBranchException() {
      Debug.print("GlobalRecordInvalidForCurrentBranchException Constructor");
   }

   public GlobalRecordInvalidForCurrentBranchException(String msg) {
      super(msg);
      Debug.print("GlobalRecordInvalidForCurrentBranchException Constructor");
   }
}
