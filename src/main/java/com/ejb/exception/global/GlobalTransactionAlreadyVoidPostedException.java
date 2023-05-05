package com.ejb.exception.global;

import com.util.Debug;

public class GlobalTransactionAlreadyVoidPostedException extends Exception {

   public GlobalTransactionAlreadyVoidPostedException() {
      Debug.print("GlobalTransactionAlreadyVoidPostedException Constructor");
   }

   public GlobalTransactionAlreadyVoidPostedException(String msg) {
      super(msg);
      Debug.print("GlobalTransactionAlreadyVoidPostedException Constructor");
   }
}
