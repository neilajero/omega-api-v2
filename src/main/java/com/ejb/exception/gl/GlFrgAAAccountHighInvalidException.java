package com.ejb.exception.gl;

import com.util.Debug;

public class GlFrgAAAccountHighInvalidException extends Exception {

    public GlFrgAAAccountHighInvalidException() {
      Debug.print("GlFrgAAAccountHighInvalidException Constructor");
   }

    public GlFrgAAAccountHighInvalidException(String msg) {
      super(msg);
      Debug.print("GlFrgAAAccountHighInvalidException Constructor");
   }
}
