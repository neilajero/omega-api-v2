package com.ejb.exception.global;

import com.util.Debug;

public class GlobalNotAllTransactionsArePostedException extends Exception {

   public GlobalNotAllTransactionsArePostedException() {
      Debug.print("GlobalNotAllTransactionsArePostedException Constructor");
   }

   public GlobalNotAllTransactionsArePostedException(String msg) {
      super(msg);
      Debug.print("GlobalNotAllTransactionsArePostedException Constructor");
   }
}
