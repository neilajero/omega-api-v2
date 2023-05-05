package com.ejb.exception.global;

import com.util.Debug;

public class GlobalTransactionAlreadyVoidException extends Exception {

   public GlobalTransactionAlreadyVoidException() {
      Debug.print("GlobalTransactionAlreadyVoidException Constructor");
   }

   public GlobalTransactionAlreadyVoidException(String msg) {
      super(msg);
      Debug.print("GlobalTransactionAlreadyVoidException Constructor");
   }
}
