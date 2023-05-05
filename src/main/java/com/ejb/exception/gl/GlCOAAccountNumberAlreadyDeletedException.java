package com.ejb.exception.gl;

import com.util.Debug;

public class GlCOAAccountNumberAlreadyDeletedException extends Exception {

   public GlCOAAccountNumberAlreadyDeletedException() {
      Debug.print("GlCOAAccountNumberAlreadyDeletedException Constructor");
   }

   public GlCOAAccountNumberAlreadyDeletedException(String msg) {
      super(msg);
      Debug.print("GlCOAAccountNumberAlreadyDeletedException Constructor");
   }
}
