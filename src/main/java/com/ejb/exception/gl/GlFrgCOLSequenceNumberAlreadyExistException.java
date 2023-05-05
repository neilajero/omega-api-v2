package com.ejb.exception.gl;

import com.util.Debug;

public class GlFrgCOLSequenceNumberAlreadyExistException extends Exception {

    public GlFrgCOLSequenceNumberAlreadyExistException() {
      Debug.print("GlFrgCOLSequenceNumberAlreadyExistException Constructor");
   }

    public GlFrgCOLSequenceNumberAlreadyExistException(String msg) {
      super(msg);
      Debug.print("GlFrgCOLSequenceNumberAlreadyExistException Constructor");
   }
}
