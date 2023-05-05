package com.ejb.exception.gl;

import com.util.Debug;

public class GlBGAPasswordInvalidException extends Exception {

   public GlBGAPasswordInvalidException() {
      Debug.print("GlBGAPasswordInvalidException Constructor");
   }

   public GlBGAPasswordInvalidException(String msg) {
      super(msg);
      Debug.print("GlBGAPasswordInvalidException Constructor");
   }
}
