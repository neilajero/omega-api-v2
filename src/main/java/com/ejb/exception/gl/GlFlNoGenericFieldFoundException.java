package com.ejb.exception.gl;

import com.util.Debug;

public class GlFlNoGenericFieldFoundException extends Exception {

   public GlFlNoGenericFieldFoundException() {
      Debug.print("GlFlNoGenericFieldFoundException Constructor");
   }

   public GlFlNoGenericFieldFoundException(String msg) {
      super(msg);
      Debug.print("GlFlNoGenericFieldFoundException Constructor");
   }
}
