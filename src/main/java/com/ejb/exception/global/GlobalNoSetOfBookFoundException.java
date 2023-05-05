package com.ejb.exception.global;

import com.util.Debug;

public class GlobalNoSetOfBookFoundException extends Exception {

   public GlobalNoSetOfBookFoundException() {
      Debug.print("GlobalNoSetOfBookFoundException Constructor");
   }

   public GlobalNoSetOfBookFoundException(String msg) {
      super(msg);
      Debug.print("GlobalNoSetOfBookFoundException Constructor");
   }
}
