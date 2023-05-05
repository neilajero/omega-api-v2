package com.ejb.exception.global;

import com.util.Debug;

public class GlobalTransactionAlreadyPostedException extends Exception {

   public GlobalTransactionAlreadyPostedException() {
      Debug.print("GlobalTransactionAlreadyPostedException Constructor");
   }

   public GlobalTransactionAlreadyPostedException(String msg) {
      super(msg);
      Debug.print("GlobalTransactionAlreadyPostedException Constructor");
   }
}
