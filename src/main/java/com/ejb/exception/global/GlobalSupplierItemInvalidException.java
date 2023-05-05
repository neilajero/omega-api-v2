package com.ejb.exception.global;

import com.util.Debug;

public class GlobalSupplierItemInvalidException extends Exception {
	
   public GlobalSupplierItemInvalidException() {
      Debug.print("GlobalSupplierItemInvalidException Constructor");
   }

   public GlobalSupplierItemInvalidException(String msg) {
      super(msg);
      Debug.print("GlobalSupplierItemInvalidException Constructor");
   }
   
}
