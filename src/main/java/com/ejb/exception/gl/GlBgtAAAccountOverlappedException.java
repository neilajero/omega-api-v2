package com.ejb.exception.gl;

import com.util.Debug;

public class GlBgtAAAccountOverlappedException extends Exception {

   public GlBgtAAAccountOverlappedException() {
      Debug.print("GlBgtAAAccountOverlappedException Constructor");
   }

   public GlBgtAAAccountOverlappedException(String msg) {
      super(msg);
      Debug.print("GlBgtAAAccountOverlappedException Constructor");
   }
}
