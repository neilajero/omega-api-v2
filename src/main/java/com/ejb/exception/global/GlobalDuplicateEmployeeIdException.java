package com.ejb.exception.global;

import com.util.Debug;

public class GlobalDuplicateEmployeeIdException  extends Exception {
	 public GlobalDuplicateEmployeeIdException() {
	      Debug.print("GlobalDuplicateEmployeeIdException Constructor");
	   }

	   public GlobalDuplicateEmployeeIdException(String msg) {
	      super(msg);
	      Debug.print("GlobalDuplicateEmployeeIdException Constructor");
	   }
}
