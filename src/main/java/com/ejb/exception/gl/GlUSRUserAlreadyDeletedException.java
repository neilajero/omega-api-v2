package com.ejb.exception.gl;

import com.util.Debug;

public class GlUSRUserAlreadyDeletedException extends Exception {

   public GlUSRUserAlreadyDeletedException() {
      Debug.print("GlUSRUserAlreadyDeletedException Constructor");
   }

   public GlUSRUserAlreadyDeletedException(String msg) {
      super(msg);
      Debug.print("GlUSRUserAlreadyDeletedException Constructor");
   }
}
