package com.ejb.exception.inv;

import com.util.Debug;

public class InvAccumulatedDepreciationAccountNotFoundException extends Exception {

   public InvAccumulatedDepreciationAccountNotFoundException() {
      Debug.print("InvAccumulatedDepreciationAccountNotFoundException Constructor");
   }

   public InvAccumulatedDepreciationAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("InvAccumulatedDepreciationAccountNotFoundException Constructor");
   }
}
