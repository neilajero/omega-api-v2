package com.ejb.exception.ar;

import com.util.Debug;

public class ArReceiptInvoiceAlreadyPaidException extends Exception {

   public ArReceiptInvoiceAlreadyPaidException() {
      Debug.print("ArReceiptInvoiceAlreadyPaidException Constructor");
   }

   public ArReceiptInvoiceAlreadyPaidException(String msg) {
      super(msg);
      Debug.print("ArReceiptInvoiceAlreadyPaidException Constructor");
   }
}
