package com.ejb.exception.global;

import com.util.Debug;

public class GlobalInvCSTRemainingQuantityIsLessThanZeroException extends Exception {

   public GlobalInvCSTRemainingQuantityIsLessThanZeroException() {
      Debug.print("GlobalInvCSTRemainingQuantityIsLessThanZeroException Constructor");
   }

   public GlobalInvCSTRemainingQuantityIsLessThanZeroException(String msg) {
      super(msg);
      Debug.print("GlobalInvCSTRemainingQuantityIsLessThanZeroException Constructor");
   }
}
