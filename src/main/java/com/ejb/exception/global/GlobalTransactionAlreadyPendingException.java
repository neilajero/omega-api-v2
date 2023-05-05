package com.ejb.exception.global;

import com.util.Debug;

public class GlobalTransactionAlreadyPendingException extends Exception {

   public GlobalTransactionAlreadyPendingException() {
      Debug.print("GlobalTransactionAlreadyPendingException Constructor");
   }

   public GlobalTransactionAlreadyPendingException(String msg) {
      super(msg);
      Debug.print("GlobalTransactionAlreadyPendingException Constructor");
   }
}
