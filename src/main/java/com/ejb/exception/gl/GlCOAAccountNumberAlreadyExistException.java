package com.ejb.exception.gl;

import com.util.Debug;

public class GlCOAAccountNumberAlreadyExistException extends Exception {

   public GlCOAAccountNumberAlreadyExistException() { 
      Debug.print("GlCOAAccountNumberAlreadyExistException Constructor");
   }

   public GlCOAAccountNumberAlreadyExistException(String msg) {
      super(msg);
      Debug.print("GlCOAAccountNumberAlreadyExistException Constructor");
   }
}
