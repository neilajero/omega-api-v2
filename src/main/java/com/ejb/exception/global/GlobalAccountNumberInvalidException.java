package com.ejb.exception.global;

import com.util.Debug;

public class GlobalAccountNumberInvalidException extends Exception {

   public GlobalAccountNumberInvalidException() {
      Debug.print("GlobalAccountNumberInvalidException Constructor");
   }

   public GlobalAccountNumberInvalidException(String msg) {
      super(msg);
      Debug.print("GlobalAccountNumberInvalidException Constructor");
   }
}
