package com.ejb.exception.global;

import com.util.Debug;

public class GlobalDocumentNumberNotUniqueException extends Exception {

   public GlobalDocumentNumberNotUniqueException() {
      Debug.print("GlobalDocumentNumberNotUniqueException Constructor");
   }

   public GlobalDocumentNumberNotUniqueException(String msg) {
      super(msg);
      Debug.print("GlobalDocumentNumberNotUniqueException Constructor");
   }
}
