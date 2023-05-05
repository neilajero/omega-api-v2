package com.ejb.exception.inv;

import com.util.Debug;

public class InvFixedCostAccountNotFoundException extends Exception {

   public InvFixedCostAccountNotFoundException() {
      Debug.print("InvFixedCostAccountNotFoundException Constructor");
   }

   public InvFixedCostAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("InvFixedCostAccountNotFoundException Constructor");
   }
}
