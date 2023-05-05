package com.ejb.exception.global;

import com.util.Debug;

public class GlobalMinimumAccountUnitInvalidException extends Exception {

   public GlobalMinimumAccountUnitInvalidException() {
      Debug.print("GlobalMinimumAccountUnitInvalidException Constructor");
   }

   public GlobalMinimumAccountUnitInvalidException(String msg) {
      super(msg);
      Debug.print("GlobalMinimumAccountUnitInvalidException Constructor");
   }
}
