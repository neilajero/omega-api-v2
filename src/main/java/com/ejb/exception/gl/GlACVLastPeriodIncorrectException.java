package com.ejb.exception.gl;

import com.util.Debug;

public class GlACVLastPeriodIncorrectException extends Exception {

   public GlACVLastPeriodIncorrectException() {
      Debug.print("GlACVLastPeriodIncorrectException Constructor");
   }

   public GlACVLastPeriodIncorrectException(String msg) {
      super(msg);
      Debug.print("GlACVLastPeriodIncorrectException Constructor");
   }
}
