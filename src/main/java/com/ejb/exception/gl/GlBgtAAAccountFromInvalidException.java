package com.ejb.exception.gl;

import com.util.Debug;

public class GlBgtAAAccountFromInvalidException extends Exception {

    public GlBgtAAAccountFromInvalidException() {
      Debug.print("GlBgtAAAccountFromInvalidException Constructor");
   }

    public GlBgtAAAccountFromInvalidException(String msg) {
      super(msg);
      Debug.print("GlBgtAAAccountFromInvalidException Constructor");
   }
}
