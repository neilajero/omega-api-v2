package com.ejb.exception.inv;

import com.util.Debug;

public class InvPowerCostAccountNotFoundException extends Exception {

   public InvPowerCostAccountNotFoundException() {
      Debug.print("InvPowerCostAccountNotFoundException Constructor");
   }

   public InvPowerCostAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("InvPowerCostAccountNotFoundException Constructor");
   }
}
