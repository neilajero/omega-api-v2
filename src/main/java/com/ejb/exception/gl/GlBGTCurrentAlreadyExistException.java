package com.ejb.exception.gl;

import com.util.Debug;

public class GlBGTCurrentAlreadyExistException extends Exception {

   public GlBGTCurrentAlreadyExistException() {
      Debug.print("GlBGTCurrentAlreadyExistException Constructor");
   }

   public GlBGTCurrentAlreadyExistException(String msg) {
      super(msg);
      Debug.print("GlBGTCurrentAlreadyExistException Constructor");
   }
}
