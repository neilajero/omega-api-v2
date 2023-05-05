package com.ejb.exception.inv;

import com.util.Debug;

public class InvILCoaGlAccruedInventoryAccountNotFoundException extends Exception {

   public InvILCoaGlAccruedInventoryAccountNotFoundException() {
      Debug.print("InvILCoaGlAccruedInventoryAccountNotFoundException Constructor");
   }

   public InvILCoaGlAccruedInventoryAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("InvILCoaGlAccruedInventoryAccountNotFoundException Constructor");
   }
}
