package com.ejb.exception.gl;

import com.util.Debug;

public class GlRepDISAccountNumberInvalidException extends Exception {

   public GlRepDISAccountNumberInvalidException() {
      Debug.print("GlRepDISAccountNumberInvalidException Constructor");
   }

   public GlRepDISAccountNumberInvalidException(String msg) {
      super(msg);
      Debug.print("GlRepDISAccountNumberInvalidException Constructor");
   }
}
