package com.ejb.exception.gl;

import com.util.Debug;

public class GlARAccountRangeInvalidException extends Exception {

   public GlARAccountRangeInvalidException() {
      Debug.print("GlARAccountRangeInvalidException Constructor");
   }

   public GlARAccountRangeInvalidException(String msg) {
      super(msg);
      Debug.print("GlARAccountRangeInvalidException Constructor");
   }
}
