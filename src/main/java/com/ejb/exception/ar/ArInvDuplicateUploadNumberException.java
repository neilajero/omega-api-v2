package com.ejb.exception.ar;

import com.util.Debug;

public class ArInvDuplicateUploadNumberException extends Exception  {
	public ArInvDuplicateUploadNumberException() {
	      Debug.print("ArInvDuplicateUploadNumberException Constructor");
	   }

	   public ArInvDuplicateUploadNumberException(String msg) {
	      super(msg);
	      Debug.print("ArInvDuplicateUploadNumberException Constructor");
	   }
}
