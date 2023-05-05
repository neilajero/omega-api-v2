package com.ejb.exception.global;

import com.util.Debug;

public class GlobalTransactionBatchCloseException extends Exception {

   public GlobalTransactionBatchCloseException() {
      Debug.print("GlobalTransactionBatchCloseException Constructor");
   }

   public GlobalTransactionBatchCloseException(String msg) {
      super(msg);
      Debug.print("GlobalTransactionBatchCloseException Constructor");
   }
}
