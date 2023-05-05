package com.ejb.exception.ar;

import com.util.Debug;

public class ArReceiptEntryValidationException extends Exception{
	 public ArReceiptEntryValidationException() {
	      Debug.print("ArReceiptEntryValidationException Constructor");
	   }

	   public ArReceiptEntryValidationException(String msg) {
	      super(msg);
	      Debug.print("ArReceiptEntryValidationException Constructor");
	   }
}
