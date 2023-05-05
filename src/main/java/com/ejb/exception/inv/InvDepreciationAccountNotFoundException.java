package com.ejb.exception.inv;

import com.util.Debug;

public class InvDepreciationAccountNotFoundException extends Exception {

   public InvDepreciationAccountNotFoundException() {
      Debug.print("InvDepreciationAccountNotFoundException Constructor");
   }

   public InvDepreciationAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("InvDepreciationAccountNotFoundException Constructor");
   }
}
