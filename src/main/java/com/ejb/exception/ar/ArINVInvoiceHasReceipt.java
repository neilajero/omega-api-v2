package com.ejb.exception.ar;

import com.util.Debug;

public class ArINVInvoiceHasReceipt extends Exception {

   public ArINVInvoiceHasReceipt() {
      Debug.print("ArINVInvoiceHasReceipt Constructor");
   }

   public ArINVInvoiceHasReceipt(String msg) {
      super(msg);
      Debug.print("ArINVInvoiceHasReceipt Constructor");
   }
}
