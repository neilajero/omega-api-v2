package com.ejb.exception.gl;

import com.util.Debug;

public class GlJREffectiveDateViolationException extends Exception {

   public GlJREffectiveDateViolationException() {
      Debug.print("GlJREffectiveDateViolationException Constructor");
   }

   public GlJREffectiveDateViolationException(String msg) {
      super(msg);
      Debug.print("GlJREffectiveDateViolationException Constructor");
   }
}
