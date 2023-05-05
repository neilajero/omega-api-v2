package com.ejb.exception.gl;

import com.util.Debug;

public class GlACVQuarterIsNotSequentialOrHasGapException extends Exception {

   public GlACVQuarterIsNotSequentialOrHasGapException() {
      Debug.print("GlACVQuarterIsNotSequentialOrHasGapException Constructor");
   }

   public GlACVQuarterIsNotSequentialOrHasGapException(String msg) {
      super(msg);
      Debug.print("GlACVQuarterIsNotSequentialOrHasGapException Constructor");
   }
}
