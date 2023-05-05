package com.ejb.exception.gl;

import com.util.Debug;

public class GlSASuspenseAccountAlreadyExistException extends Exception {

   public GlSASuspenseAccountAlreadyExistException() {
      Debug.print("GlSASuspenseAccountAlreadyExistException Constructor");
   }

   public GlSASuspenseAccountAlreadyExistException(String msg) {
      super(msg);
      Debug.print("GlSASuspenseAccountAlreadyExistException Constructor");
   }
}
