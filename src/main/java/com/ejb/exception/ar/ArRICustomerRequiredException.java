package com.ejb.exception.ar;

import com.util.Debug;

public class ArRICustomerRequiredException extends Exception {

   public ArRICustomerRequiredException() {
      Debug.print("ArRICustomerRequiredException Constructor");
   }

   public ArRICustomerRequiredException(String msg) {
      super(msg);
      Debug.print("ArRICustomerRequiredException Constructor");
   }
}
