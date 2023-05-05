package com.ejb.exception.gl;

import com.util.Debug;

public class GlARAccountRangeOverlappedException extends Exception {

   public GlARAccountRangeOverlappedException() {
      Debug.print("GlARAccountRangeOverlappedException Constructor");
   }

   public GlARAccountRangeOverlappedException(String msg) {
      super(msg);
      Debug.print("GlARAccountRangeOverlappedException Constructor");
   }
}
