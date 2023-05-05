package com.ejb.exception.gl;

import com.util.Debug;

public class GlACVDateIsNotSequentialOrHasGapException extends Exception {

   public GlACVDateIsNotSequentialOrHasGapException() {
      Debug.print("GlACVDateIsNotSequentialOrHasGapException Constructor");
   }

   public GlACVDateIsNotSequentialOrHasGapException(String msg) {
      super(msg);
      Debug.print("GlACVDateIsNotSequentialOrHasGapException Constructor");
   }
}
