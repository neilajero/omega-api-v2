package com.ejb.exception.ad;

import com.util.Debug;

public class AdAUUserCannotBeAnApproverException extends Exception {

   public AdAUUserCannotBeAnApproverException() {
      Debug.print("AdAUUserCannotBeAnApproverException Constructor");
   }

   public AdAUUserCannotBeAnApproverException(String msg) {
      super(msg);
      Debug.print("AdAUUserCannotBeAnApproverException Constructor");
   }
}
