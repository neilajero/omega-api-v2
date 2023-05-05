package com.ejb.exception.gl;

import com.util.Debug;

public class GlARAccountNumberOfSegmentInvalidException extends Exception {

   public GlARAccountNumberOfSegmentInvalidException() {
      Debug.print("GlARAccountNumberOfSegmentInvalidException Constructor");
   }

   public GlARAccountNumberOfSegmentInvalidException(String msg) {
      super(msg);
      Debug.print("GlARAccountNumberOfSegmentInvalidException Constructor");
   }
}
