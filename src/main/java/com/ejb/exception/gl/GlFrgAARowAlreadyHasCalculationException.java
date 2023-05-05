package com.ejb.exception.gl;

import com.util.Debug;

public class GlFrgAARowAlreadyHasCalculationException extends Exception {

    public GlFrgAARowAlreadyHasCalculationException() {
      Debug.print("GlFrgAARowAlreadyHasCalculationException Constructor");
   }

    public GlFrgAARowAlreadyHasCalculationException(String msg) {
      super(msg);
      Debug.print("GlFrgAARowAlreadyHasCalculationException Constructor");
   }
}
