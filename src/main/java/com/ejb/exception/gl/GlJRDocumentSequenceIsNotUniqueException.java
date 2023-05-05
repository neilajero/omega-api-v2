package com.ejb.exception.gl;

import com.util.Debug;

public class GlJRDocumentSequenceIsNotUniqueException extends Exception {

   public GlJRDocumentSequenceIsNotUniqueException() {
      Debug.print("GlJRDocumentSequenceIsNotUniqueException Constructor");
   }

   public GlJRDocumentSequenceIsNotUniqueException(String msg) {
      super(msg);
      Debug.print("GlJRDocumentSequenceIsNotUniqueException Constructor");
   }
}
