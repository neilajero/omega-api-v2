package com.ejb.exception.global;

import com.util.Debug;

public class InsufficientCreditBalanceException extends Exception {
	public InsufficientCreditBalanceException() {
	      Debug.print("InsufficientCreditBalanceException Constructor");
	   }

	   public InsufficientCreditBalanceException(String msg) {
	      super(msg);
	      Debug.print("InsufficientCreditBalanceException Constructor");
	   }
}
