package com.ejb.exception.inv;

import com.util.Debug;

public class InvLaborCostAccountNotFoundException extends Exception {

   public InvLaborCostAccountNotFoundException() {
      Debug.print("InvLaborCostAccountNotFoundException Constructor");
   }

   public InvLaborCostAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("InvLaborCostAccountNotFoundException Constructor");
   }
}
