package com.ejb.exception.gl;

import com.util.Debug;

public class GlUSRUserAlreadyExistException extends Exception {

   public GlUSRUserAlreadyExistException() {
      Debug.print("GlUSRUserAlreadyExistException Constructor");
   }

   public GlUSRUserAlreadyExistException(String msg) {
      super(msg);
      Debug.print("GlUSRUserAlreadyExistException Constructor");
   }
}
