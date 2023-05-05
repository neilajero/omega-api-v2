package com.ejb.exception.global;

import com.util.Debug;

public class GlobalTransactionAlreadyApprovedException extends Exception {

   public GlobalTransactionAlreadyApprovedException() {
      Debug.print("GlobalTransactionAlreadyApprovedException Constructor");
   }

   public GlobalTransactionAlreadyApprovedException(String msg) {
      super(msg);
      Debug.print("GlobalTransactionAlreadyApprovedException Constructor");
   }
}
