package com.ejb.exception.gl;

import com.util.Debug;

public class GlRESResponsibilityAlreadyDeletedException extends Exception {

   public GlRESResponsibilityAlreadyDeletedException() {
      Debug.print("GlRESResponsibilityAlreadyDeletedException Constructor");
   }

   public GlRESResponsibilityAlreadyDeletedException(String msg) {
      super(msg);
      Debug.print("GlRESResponsibilityAlreadyDeletedException Constructor");
   }
}
