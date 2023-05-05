package com.ejb.exception.ap;

import com.util.Debug;

public class ApCHKCheckNumberNotUniqueException extends Exception {

   public ApCHKCheckNumberNotUniqueException() {
      Debug.print("ApCHKCheckNumberNotUniqueException Constructor");
   }

   public ApCHKCheckNumberNotUniqueException(String msg) {
      super(msg);
      Debug.print("ApCHKCheckNumberNotUniqueException Constructor");
   }
}
