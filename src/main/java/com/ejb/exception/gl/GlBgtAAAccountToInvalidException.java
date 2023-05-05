package com.ejb.exception.gl;

import com.util.Debug;

public class GlBgtAAAccountToInvalidException extends Exception {

    public GlBgtAAAccountToInvalidException() {
      Debug.print("GlBgtAAAccountToInvalidException Constructor");
   }

    public GlBgtAAAccountToInvalidException(String msg) {
      super(msg);
      Debug.print("GlBgtAAAccountToInvalidException Constructor");
   }
}
