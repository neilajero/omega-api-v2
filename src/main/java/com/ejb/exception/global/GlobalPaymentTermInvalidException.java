package com.ejb.exception.global;

import com.util.Debug;

public class GlobalPaymentTermInvalidException extends Exception {

   public GlobalPaymentTermInvalidException() {
      Debug.print("GlobalPaymentTermInvalidException Constructor");
   }

   public GlobalPaymentTermInvalidException(String msg) {
      super(msg);
      Debug.print("GlobalPaymentTermInvalidException Constructor");
   }
}
