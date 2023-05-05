package com.ejb.exception.gl;

import com.util.Debug;

public class GlRESNoResponsibilityFoundException extends Exception {

   public GlRESNoResponsibilityFoundException() {
      Debug.print("GlRESNoResponsibilityFoundException Constructor");
   }

   public GlRESNoResponsibilityFoundException(String msg) {
      super(msg);
      Debug.print("GlRESNoResponsibilityFoundException Constructor");
   }
}
