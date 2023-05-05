package com.ejb.exception.gl;

import com.util.Debug;

public class GlSANoSuspenseAccountFoundException extends Exception {

   public GlSANoSuspenseAccountFoundException() {
      Debug.print("GlSANoSuspenseAccountFoundException Constructor");
   }

   public GlSANoSuspenseAccountFoundException(String msg) {
      super(msg);
      Debug.print("GlSANoSuspenseAccountFoundException Constructor");
   }
}
