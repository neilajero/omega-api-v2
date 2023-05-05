package com.ejb.exception.gl;

import com.util.Debug;

public class GlSASuspenseAccountAlreadyDeletedException extends Exception {

   public GlSASuspenseAccountAlreadyDeletedException() {
      Debug.print("GlSASuspenseAccountAlreadyDeletedException Constructor");
   }

   public GlSASuspenseAccountAlreadyDeletedException(String msg) {
      super(msg);
      Debug.print("GlSASuspenseAccountAlreadyDeletedException Constructor");
   }
}
