package com.ejb.exception.gl;

import com.util.Debug;

public class GlFrgCOLColumnNameAlreadyExistException extends Exception {

    public GlFrgCOLColumnNameAlreadyExistException() {
      Debug.print("GlFrgCOLColumnNameAlreadyExistExceptionn Constructor");
   }

    public GlFrgCOLColumnNameAlreadyExistException(String msg) {
      super(msg);
      Debug.print("GlFrgCOLColumnNameAlreadyExistException Constructor");
   }
}
