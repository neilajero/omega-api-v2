package com.ejb.exception.gl;

import com.util.Debug;

public class GlPTPeriodTypeAlreadyDeletedException extends Exception {

   public GlPTPeriodTypeAlreadyDeletedException() {
      Debug.print("GlPTPeriodTypeAlreadyDeletedException Constructor");
   }

   public GlPTPeriodTypeAlreadyDeletedException(String msg) {
      super(msg);
      Debug.print("GlPTPeriodTypeAlreadyDeletedException Constructor");
   }
}
