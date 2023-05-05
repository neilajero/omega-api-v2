package com.ejb.exception.gl;

import com.util.Debug;

public class GlARAccountRangeAlreadyDeletedException extends Exception {

   public GlARAccountRangeAlreadyDeletedException() {
      Debug.print("GlARAccountRangeAlreadyDeletedException Constructor");
   }

   public GlARAccountRangeAlreadyDeletedException(String msg) {
      super(msg);
      Debug.print("GlARAccountRangeAlreadyDeletedException Constructor");
   }
}
