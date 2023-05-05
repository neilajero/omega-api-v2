package com.ejb.exception.global;

import com.util.Debug;

public class GlobalBranchAccountNumberInvalidException extends Exception {

   private int LineNumberError;
	   
   public GlobalBranchAccountNumberInvalidException() {
      Debug.print("GlobalBranchAccountNumberInvalidException Constructor");
   }

   public GlobalBranchAccountNumberInvalidException(String msg) {
      super(msg);
      Debug.print("GlobalBranchAccountNumberInvalidException Constructor");
   }
   
   public GlobalBranchAccountNumberInvalidException(String msg, int LineNumberError) {
	   super(msg);
	   this.LineNumberError = LineNumberError;	   	   
	   Debug.print("GlobalBranchAccountNumberInvalidException Constructor");
   }
   
   public void setLineNumberError(int LineNumberError){
	   this.LineNumberError = LineNumberError;
   }
   
   public String getLineNumberError(){
	   return String.valueOf(this.LineNumberError);
   }
}
