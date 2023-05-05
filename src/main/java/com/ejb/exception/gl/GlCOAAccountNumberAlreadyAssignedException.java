package com.ejb.exception.gl;

import com.util.Debug;

public class GlCOAAccountNumberAlreadyAssignedException extends Exception {

   public GlCOAAccountNumberAlreadyAssignedException() {
      Debug.print("GlCOAAccountNumberAlreadyAssignedException Constructor");
   }

   public GlCOAAccountNumberAlreadyAssignedException(String msg) {
      super(msg);
      Debug.print("GlCOAAccountNumberAlreadyAssignedException Constructor");
   }
}
