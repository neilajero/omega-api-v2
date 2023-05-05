package com.ejb.exception.gl;

import com.util.Debug;

public class GlCOAAccountNumberIsInvalidException extends Exception {

   public GlCOAAccountNumberIsInvalidException() {
      Debug.print("GlCOAAccountNumberIsInvalidException Constructor");
   }

   public GlCOAAccountNumberIsInvalidException(String msg) {
      super(msg);
      Debug.print("GlCOAAccountNumberIsInvalidException Constructor");
   }
}
