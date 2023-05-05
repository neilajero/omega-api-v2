package com.ejb.exception.ad;

import com.util.Debug;

public class AdBRBranchCodeAlreadyExistsException extends Exception {

   public AdBRBranchCodeAlreadyExistsException() {
      Debug.print("AdBRBranchCodeAlreadyExistsException Constructor");
   }

   public AdBRBranchCodeAlreadyExistsException(String msg) {
      super(msg);
      Debug.print("AdBRBranchCodeAlreadyExistsException Constructor");
   }
}
