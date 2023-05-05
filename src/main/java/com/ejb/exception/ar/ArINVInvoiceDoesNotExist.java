package com.ejb.exception.ar;

import com.util.Debug;

public class ArINVInvoiceDoesNotExist extends Exception {

   public ArINVInvoiceDoesNotExist() {
      Debug.print("ArINVInvoiceDoesNotExist Constructor");
   }

   public ArINVInvoiceDoesNotExist(String msg) {
      super(msg);
      Debug.print("ArINVInvoiceDoesNotExist Constructor");
   }
}
