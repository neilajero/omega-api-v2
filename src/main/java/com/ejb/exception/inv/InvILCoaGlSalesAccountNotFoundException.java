package com.ejb.exception.inv;

import com.util.Debug;

public class InvILCoaGlSalesAccountNotFoundException extends Exception {

   public InvILCoaGlSalesAccountNotFoundException() {
      Debug.print("InvILCoaGlSalesAccountNotFoundException Constructor");
   }

   public InvILCoaGlSalesAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("InvILCoaGlSalesAccountNotFoundException Constructor");
   }
}
