package com.ejb.exception.global;

import com.util.Debug;

public class GlobalTransactionAlreadyLockedException extends Exception {

   public GlobalTransactionAlreadyLockedException() {
      Debug.print("GlobalTransactionAlreadyLockedException Constructor");
   }

   public GlobalTransactionAlreadyLockedException(String msg) {
      super(msg);
      Debug.print("GlobalTransactionAlreadyLockedException Constructor");
   }
}
