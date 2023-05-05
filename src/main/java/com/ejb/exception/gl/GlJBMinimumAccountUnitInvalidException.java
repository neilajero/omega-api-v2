package com.ejb.exception.gl;

import com.util.Debug;

public class GlJBMinimumAccountUnitInvalidException extends Exception {

   public GlJBMinimumAccountUnitInvalidException() {
      Debug.print("GlJBMinimumAccountUnitInvalidException Constructor");
   }

   public GlJBMinimumAccountUnitInvalidException(String msg) {
      super(msg);
      Debug.print("GlJBMinimumAccountUnitInvalidException Constructor");
   }
}
