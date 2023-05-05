package com.ejb.exception.gl;

import com.util.Debug;

public class GlARResponsibilityNotAllowedException extends Exception {

   public GlARResponsibilityNotAllowedException() {
      Debug.print("GlARResponsibilityNotAllowedException Constructor");
   }

   public GlARResponsibilityNotAllowedException(String msg) {
      super(msg);
      Debug.print("GlARResponsibilityNotAllowedException Constructor");
   }
}
