package com.ejb.exception.gl;

import com.util.Debug;

public class GlRESResponsibilityAlreadyExistException extends Exception {

   public GlRESResponsibilityAlreadyExistException() {
      Debug.print("GlRESResponsibilityAlreadyExistException Constructor");
   }

   public GlRESResponsibilityAlreadyExistException(String msg) {
      super(msg);
      Debug.print("GlRESResponsibilityAlreadyExistException Constructor");
   }
}
