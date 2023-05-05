package com.ejb.exception.ar;

import com.util.Debug;

public class ArINVAmountExceedsCreditLimitException extends Exception {

   public ArINVAmountExceedsCreditLimitException() {
      Debug.print("ArINVAmountExceedsCreditLimitException Constructor");
   }

   public ArINVAmountExceedsCreditLimitException(String msg) {
      super(msg);
      Debug.print("ArINVAmountExceedsCreditLimitException Constructor");
   }
}
