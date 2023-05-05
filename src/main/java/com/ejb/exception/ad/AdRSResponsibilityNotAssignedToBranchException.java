package com.ejb.exception.ad;

import com.util.Debug;

public class AdRSResponsibilityNotAssignedToBranchException extends Exception {

   public AdRSResponsibilityNotAssignedToBranchException() {
      Debug.print("AdRSResponsibilityNotAssignedToBranchException Constructor");
   }

   public AdRSResponsibilityNotAssignedToBranchException(String msg) {
      super(msg);
      Debug.print("AdRSResponsibilityNotAssignedToBranchException Constructor");
   }
   
}
