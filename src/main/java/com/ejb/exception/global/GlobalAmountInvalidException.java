package com.ejb.exception.global;

import com.util.Debug;

public class GlobalAmountInvalidException extends Exception {

   public GlobalAmountInvalidException() {
      Debug.print("GlobalAmountInvalidException Constructor");
   }

   public GlobalAmountInvalidException(String msg) {
      super(msg);
      Debug.print("GlobalAmountInvalidException Constructor");
   }
}
