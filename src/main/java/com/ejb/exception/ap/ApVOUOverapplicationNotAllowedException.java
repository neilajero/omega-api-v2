package com.ejb.exception.ap;

import com.util.Debug;

public class ApVOUOverapplicationNotAllowedException extends Exception {

   public ApVOUOverapplicationNotAllowedException() {
      Debug.print("ApVOUOverapplicationNotAllowedException Constructor");
   }

   public ApVOUOverapplicationNotAllowedException(String msg) {
      super(msg);
      Debug.print("ApVOUOverapplicationNotAllowedException Constructor");
   }
}
