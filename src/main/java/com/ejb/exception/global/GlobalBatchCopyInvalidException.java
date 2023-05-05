package com.ejb.exception.global;

import com.util.Debug;

public class GlobalBatchCopyInvalidException extends Exception {

   public GlobalBatchCopyInvalidException() {
      Debug.print("GlobalBatchCopyInvalidException Constructor");
   }

   public GlobalBatchCopyInvalidException(String msg) {
      super(msg);
      Debug.print("GlobalBatchCopyInvalidException Constructor");
   }
}
