package com.ejb.exception.gl;

import com.util.Debug;

public class GlFrgAAAccountLowInvalidException extends Exception {

    public GlFrgAAAccountLowInvalidException() {
      Debug.print("GlFrgAAAccountLowInvalidException Constructor");
   }

    public GlFrgAAAccountLowInvalidException(String msg) {
      super(msg);
      Debug.print("GlFrgAAAccountLowInvalidException Constructor");
   }
}
