package com.ejb.exception.gl;

import com.util.Debug;

public class GlSASourceCategoryCombinationAlreadyExistException extends Exception {

   public GlSASourceCategoryCombinationAlreadyExistException() {
      Debug.print("GlSASourceCategoryCombinationAlreadyExistException Constructor");
   }

   public GlSASourceCategoryCombinationAlreadyExistException(String msg) {
      super(msg);
      Debug.print("GlSASourceCategoryCombinationAlreadyExistException Constructor");
   }
}
