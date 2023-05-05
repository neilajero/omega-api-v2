package com.ejb.exception.global;

import com.util.Debug;

public class GlobalOverapplicationNotAllowedException extends Exception {

   public GlobalOverapplicationNotAllowedException() {
      Debug.print("GlobalOverapplicationNotAllowedException Constructor");
   }

   public GlobalOverapplicationNotAllowedException(String msg) {
      super(msg);
      Debug.print("GlobalOverapplicationNotAllowedException Constructor");
   }
}
