package com.ejb.exception.gl;

import com.util.Debug;

public class GlFrgROWRowNameAlreadyExistException extends Exception {

    public GlFrgROWRowNameAlreadyExistException() {
      Debug.print("GlFrgROWRowNameAlreadyExistException Constructor");
   }

    public GlFrgROWRowNameAlreadyExistException(String msg) {
      super(msg);
      Debug.print("GlFrgROWRowNameAlreadyExistException Constructor");
   }
}
