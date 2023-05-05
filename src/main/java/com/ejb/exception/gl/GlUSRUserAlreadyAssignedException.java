package com.ejb.exception.gl;

import com.util.Debug;

public class GlUSRUserAlreadyAssignedException extends Exception {

   public GlUSRUserAlreadyAssignedException() {
      Debug.print("GlUSRUserAlreadyAssignedException Constructor");
   }

   public GlUSRUserAlreadyAssignedException(String msg) {
      super(msg);
      Debug.print("GlUSRUserAlreadyAssignedException Constructor");
   }
}
