package com.ejb.exception.gl;

import com.util.Debug;

public class GlUSRAmountLimitInvalidException extends Exception {

   public GlUSRAmountLimitInvalidException() {
      Debug.print("GlUSRAmountLimitInvalidException Constructor");
   }

   public GlUSRAmountLimitInvalidException(String msg) {
      super(msg);
      Debug.print("GlUSRAmountLimitInvalidException Constructor");
   }
}
