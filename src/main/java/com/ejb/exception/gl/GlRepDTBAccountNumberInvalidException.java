package com.ejb.exception.gl;

import com.util.Debug;

public class GlRepDTBAccountNumberInvalidException extends Exception {

   public GlRepDTBAccountNumberInvalidException() {
      Debug.print("GlRepDTBAccountNumberInvalidException Constructor");
   }

   public GlRepDTBAccountNumberInvalidException(String msg) {
      super(msg);
      Debug.print("GlRepDTBAccountNumberInvalidException Constructor");
   }
}
