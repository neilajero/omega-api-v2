package com.ejb.exception.inv;

import com.util.Debug;

public class InvILCoaGlCostOfSalesAccountNotFoundException extends Exception {

   public InvILCoaGlCostOfSalesAccountNotFoundException() {
      Debug.print("InvILCoaGlCostOfSalesAccountNotFoundException Constructor");
   }

   public InvILCoaGlCostOfSalesAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("InvILCoaGlCostOfSalesAccountNotFoundException Constructor");
   }
}
