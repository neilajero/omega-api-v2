package com.ejb.exception.gl;

import com.util.Debug;

public class GlRJLMinimumAccountUnitInvalidException extends Exception {

   public GlRJLMinimumAccountUnitInvalidException() {
      Debug.print("GlRJLMinimumAccountUnitInvalidException Constructor");
   }

   public GlRJLMinimumAccountUnitInvalidException(String msg) {
      super(msg);
      Debug.print("GlRJLMinimumAccountUnitInvalidException Constructor");
   }
}
