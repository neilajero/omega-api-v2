package com.ejb.exception.global;

import com.util.Debug;


public class GlobalDuplicateCustomerCodeException extends Exception {
	 public GlobalDuplicateCustomerCodeException() {
	      Debug.print("GlobalDuplicateCustomerCodeException Constructor");
	   }

	   public GlobalDuplicateCustomerCodeException(String msg) {
	      super(msg);
	      Debug.print("GlobalDuplicateCustomerCodeException Constructor");
	   }
}
