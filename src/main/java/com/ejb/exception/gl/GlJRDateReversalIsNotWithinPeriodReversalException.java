package com.ejb.exception.gl;

import com.util.Debug;

public class GlJRDateReversalIsNotWithinPeriodReversalException extends Exception {

   public GlJRDateReversalIsNotWithinPeriodReversalException() {
      Debug.print("GlJRDateReversalIsNotWithinPeriodReversalException Constructor");
   }

   public GlJRDateReversalIsNotWithinPeriodReversalException(String msg) {
      super(msg);
      Debug.print("GlJRDateReversalIsNotWithinPeriodReversalException Constructor");
   }
}
