package com.ejb.exception.global;

import com.util.Debug;

public class GlobalCoaAccountNotFoundException extends Exception {

   public GlobalCoaAccountNotFoundException() {
      Debug.print("GlobalCoaAccountNotFoundException Constructor");
   }

   public GlobalCoaAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("GlobalCoaAccountNotFoundException Constructor");
   }
}
