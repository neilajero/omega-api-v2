package com.ejb.exception.gl;

import com.util.Debug;

public class GlARAccountRangeNoAccountFoundException extends Exception {

   public GlARAccountRangeNoAccountFoundException() {
      Debug.print("GlARAccountRangeNoAccountFoundException Constructor");
   }

   public GlARAccountRangeNoAccountFoundException(String msg) {
      super(msg);
      Debug.print("GlARAccountRangeNoAccountFoundException Constructor");
   }
}
