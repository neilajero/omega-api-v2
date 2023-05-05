package com.ejb.exception.gl;

import com.util.Debug;

public class GlJREffectiveDateNoPeriodExistException extends Exception {

   public GlJREffectiveDateNoPeriodExistException() {
      Debug.print("GlJREffectiveDateNoPeriodExistException Constructor");
   }

   public GlJREffectiveDateNoPeriodExistException(String msg) {
      super(msg);
      Debug.print("GlJREffectiveDateNoPeriodExistException Constructor");
   }
}
