package com.ejb.exception.gl;

import com.util.Debug;

public class GlUSRNoUserFoundException extends Exception {

   public GlUSRNoUserFoundException() {
      Debug.print("GlUSRNoUserFoundException Constructor");
   }

   public GlUSRNoUserFoundException(String msg) {
      super(msg);
      Debug.print("GlUSRNoUserFoundException Constructor");
   }
}
