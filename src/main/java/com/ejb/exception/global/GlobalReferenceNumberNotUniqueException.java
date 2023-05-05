package com.ejb.exception.global;

import com.util.Debug;

public class GlobalReferenceNumberNotUniqueException extends Exception {

   public GlobalReferenceNumberNotUniqueException() {
      Debug.print("GlobalReferenceNumberNotUniqueException Constructor");
   }

   public GlobalReferenceNumberNotUniqueException(String msg) {
      super(msg);
      Debug.print("GlobalReferenceNumberNotUniqueException Constructor");
   }
}
