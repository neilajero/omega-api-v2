package com.ejb.exception.inv;

import com.util.Debug;

public class InvFreightCostAccountNotFoundException extends Exception {

   public InvFreightCostAccountNotFoundException() {
      Debug.print("InvFreightCostAccountNotFoundException Constructor");
   }

   public InvFreightCostAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("InvFreightCostAccountNotFoundException Constructor");
   }
}
