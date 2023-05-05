package com.ejb.exception.gl;

import com.util.Debug;

public class GlSOBNoSetOfBookFoundException extends Exception {

   public GlSOBNoSetOfBookFoundException() {
      Debug.print("GlSOBNoSetOfBookFoundException Constructor");
   }

   public GlSOBNoSetOfBookFoundException(String msg) {
      super(msg);
      Debug.print("GlSOBNoSetOfBookFoundException Constructor");
   }
}
