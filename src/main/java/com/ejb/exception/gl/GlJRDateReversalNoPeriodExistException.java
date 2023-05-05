package com.ejb.exception.gl;

import com.util.Debug;

public class GlJRDateReversalNoPeriodExistException extends Exception {

   public GlJRDateReversalNoPeriodExistException() {
      Debug.print("GlJRDateReversalNoPeriodExistException Constructor");
   }

   public GlJRDateReversalNoPeriodExistException(String msg) {
      super(msg);
      Debug.print("GlJRDateReversalNoPeriodExistException Constructor");
   }
}
