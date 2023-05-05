package com.ejb.exception.ap;

import com.util.Debug;

public class ApCHKVoucherHasNoWTaxCodeException extends Exception {

   public ApCHKVoucherHasNoWTaxCodeException() {
      Debug.print("ApCHKVoucherHasNoWTaxCodeException Constructor");
   }

   public ApCHKVoucherHasNoWTaxCodeException(String msg) {
      super(msg);
      Debug.print("ApCHKVoucherHasNoWTaxCodeException Constructor");
   }
}
