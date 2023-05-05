package com.ejb.exception.gl;

import com.util.Debug;

public class GlDepreciationPeriodInvalidException extends Exception {

   public GlDepreciationPeriodInvalidException() {
      Debug.print("GlDepreciationPeriodInvalidException Constructor");
   }

   public GlDepreciationPeriodInvalidException(String msg) {
      super(msg);
      Debug.print("GlDepreciationPeriodInvalidException Constructor");
   }
}
