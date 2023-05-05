package com.ejb.exception.gl;

import com.util.Debug;

public class GlBGTStatusAlreadyDefinedException extends Exception {

   public GlBGTStatusAlreadyDefinedException() {
      Debug.print("GlBGTStatusAlreadyDefinedException Constructor");
   }

   public GlBGTStatusAlreadyDefinedException(String msg) {
      super(msg);
      Debug.print("GlBGTStatusAlreadyDefinedException Constructor");
   }
}
