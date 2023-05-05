package com.ejb.exception.inv;

import com.util.Debug;

public class InvILCoaGlInventoryAccountNotFoundException extends Exception {

   public InvILCoaGlInventoryAccountNotFoundException() {
      Debug.print("InvILCoaGlInventoryAccountNotFoundException Constructor");
   }

   public InvILCoaGlInventoryAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("InvILCoaGlInventoryAccountNotFoundException Constructor");
   }
}
