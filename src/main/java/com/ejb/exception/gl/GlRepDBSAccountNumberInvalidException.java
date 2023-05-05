package com.ejb.exception.gl;

import com.util.Debug;

public class GlRepDBSAccountNumberInvalidException extends Exception {

   public GlRepDBSAccountNumberInvalidException() {
      Debug.print("GlRepDBSAccountNumberInvalidException Constructor");
   }

   public GlRepDBSAccountNumberInvalidException(String msg) {
      super(msg);
      Debug.print("GlRepDBSAccountNumberInvalidException Constructor");
   }
}
