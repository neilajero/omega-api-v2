package com.ejb.exception.gl;

import com.util.Debug;

public class GlCOAAccountNumberHasParentValueException extends Exception {

   public GlCOAAccountNumberHasParentValueException() {
      Debug.print("GlCOAAccountNumberHasParentException Constructor");
   }

   public GlCOAAccountNumberHasParentValueException(String msg) {
      super(msg);
      Debug.print("GlCOAAccountNumberHasParentException Constructor");
   }
}
