package com.ejb.exception.gl;

import com.util.Debug;

public class GlFrgROWLineNumberAlreadyExistException extends Exception {

    public GlFrgROWLineNumberAlreadyExistException() {
      Debug.print("GlFrgROWLineNumberAlreadyExistException Constructor");
   }

    public GlFrgROWLineNumberAlreadyExistException(String msg) {
      super(msg);
      Debug.print("GlFrgROWLineNumberAlreadyExistException Constructor");
   }
}
