package com.ejb.exception.ad;

import com.util.Debug;

public class AdAURequesterMustBeEnteredOnceException extends Exception {

   public AdAURequesterMustBeEnteredOnceException() {
      Debug.print("AdAURequesterMustBeEnteredOnceException Constructor");
   }

   public AdAURequesterMustBeEnteredOnceException(String msg) {
      super(msg);
      Debug.print("AdAURequesterMustBeEnteredOnceException Constructor");
   }
}
