package com.ejb.exception.global;

import com.util.Debug;

public class GlobalExpiryDateNotFoundException extends Exception {

   public GlobalExpiryDateNotFoundException() {
      Debug.print("Expiry Date Not Found");
   }

   public GlobalExpiryDateNotFoundException(String msg) {
      super(msg);
      Debug.print("Expiry Date Not Found");
   }
}
