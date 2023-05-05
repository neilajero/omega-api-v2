package com.ejb.exception.ar;

import com.util.Debug;

public class ArREDuplicatePayfileReferenceNumberException extends Exception {
	 public ArREDuplicatePayfileReferenceNumberException() {
	      Debug.print("ArREDuplicatePayfileReferenceNumberException Constructor");
	   }

	   public ArREDuplicatePayfileReferenceNumberException(String msg) {
	      super(msg);
	      Debug.print("ArREDuplicatePayfileReferenceNumberException Constructor");
	   }
}
