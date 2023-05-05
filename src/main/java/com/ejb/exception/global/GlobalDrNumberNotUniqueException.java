package com.ejb.exception.global;

import com.util.Debug;

public class GlobalDrNumberNotUniqueException extends Exception {

   public GlobalDrNumberNotUniqueException() {
      Debug.print("GlobalDrNumberNotUniqueException Constructor");
   }

   public GlobalDrNumberNotUniqueException(String msg) {
      super(msg);
      Debug.print("GlobalDrNumberNotUniqueException Constructor");
   }
}
