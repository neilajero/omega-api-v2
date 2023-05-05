package com.ejb.exception.ad;

import com.util.Debug;

public class AdAUUserCannotBeARequesterException extends Exception {

   public AdAUUserCannotBeARequesterException() {
      Debug.print("AdAUUserCannotBeARequesterException Constructor");
   }

   public AdAUUserCannotBeARequesterException(String msg) {
      super(msg);
      Debug.print("AdAUUserCannotBeARequesterException Constructor");
   }
}
