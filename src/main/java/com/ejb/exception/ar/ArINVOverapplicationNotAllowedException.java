package com.ejb.exception.ar;

import com.util.Debug;

public class ArINVOverapplicationNotAllowedException extends Exception {

   public ArINVOverapplicationNotAllowedException() {
      Debug.print("ArINVOverapplicationNotAllowedException Constructor");
   }

   public ArINVOverapplicationNotAllowedException(String msg) {
      super(msg);
      Debug.print("ArINVOverapplicationNotAllowedException Constructor");
   }
}
