package com.ejb.exception.inv;

import com.util.Debug;

public class InvILCoaGlSalesReturnAccountNotFoundException extends Exception {

   public InvILCoaGlSalesReturnAccountNotFoundException() {
      Debug.print("InvILCoaGlSalesReturnAccountNotFoundException Constructor");
   }

   public InvILCoaGlSalesReturnAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("InvILCoaGlSalesReturnAccountNotFoundException Constructor");
   }
}
