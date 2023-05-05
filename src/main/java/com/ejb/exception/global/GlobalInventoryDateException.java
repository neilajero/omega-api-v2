package com.ejb.exception.global;

import com.util.Debug;

public class GlobalInventoryDateException extends Exception {

   public GlobalInventoryDateException() {
      Debug.print("Invalid Date");
   }

   public GlobalInventoryDateException(String msg) {
      super(msg);
      Debug.print("Invalid Date");
   }
}
