package com.ejb.exception.gl;

import com.util.Debug;

public class GlARNoAccountRangeFoundException extends Exception {

   public GlARNoAccountRangeFoundException() {
      Debug.print("GlARNoAccountRangeFoundException Constructor");
   }

   public GlARNoAccountRangeFoundException(String msg) {
      super(msg);
      Debug.print("GlARNoAccountRangeFoundException Constructor");
   }
}
