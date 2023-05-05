package com.ejb.exception.global;

import com.util.Debug;

public class GlobalConversionDateNotExistException extends Exception {

   public GlobalConversionDateNotExistException() {
      Debug.print("GlobalConversionDateNotExistException Constructor");
   }

   public GlobalConversionDateNotExistException(String msg) {
      super(msg);
      Debug.print("GlobalConversionDateNotExistException Constructor");
   }
}
