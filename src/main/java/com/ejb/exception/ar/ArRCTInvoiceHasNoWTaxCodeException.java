package com.ejb.exception.ar;

import com.util.Debug;

public class ArRCTInvoiceHasNoWTaxCodeException extends Exception {

   public ArRCTInvoiceHasNoWTaxCodeException() {
      Debug.print("ArRCTInvoiceHasNoWTaxCodeException Constructor");
   }

   public ArRCTInvoiceHasNoWTaxCodeException(String msg) {
      super(msg);
      Debug.print("ArRCTInvoiceHasNoWTaxCodeException Constructor");
   }
}
