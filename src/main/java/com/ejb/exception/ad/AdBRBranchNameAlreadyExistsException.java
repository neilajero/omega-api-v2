package com.ejb.exception.ad;

import com.util.Debug;

public class AdBRBranchNameAlreadyExistsException extends Exception {

   public AdBRBranchNameAlreadyExistsException() {
      Debug.print("AdBRBranchNameAlreadyExistsException Constructor");
   }

   public AdBRBranchNameAlreadyExistsException(String msg) {
      super(msg);
      Debug.print("AdBRBranchNameAlreadyExistsException Constructor");
   }
}
