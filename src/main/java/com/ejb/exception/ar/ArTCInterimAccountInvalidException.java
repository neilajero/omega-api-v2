package com.ejb.exception.ar;

import com.util.Debug;

public class ArTCInterimAccountInvalidException extends Exception {

   public ArTCInterimAccountInvalidException() {
      Debug.print("ArTCInterimAccountInvalidException Constructor");
   }

   public ArTCInterimAccountInvalidException(String msg) {
      super(msg);
      Debug.print("ArTCInterimAccountInvalidException Constructor");
   }
}
