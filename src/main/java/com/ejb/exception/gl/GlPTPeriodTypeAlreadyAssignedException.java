package com.ejb.exception.gl;

import com.util.Debug;

public class GlPTPeriodTypeAlreadyAssignedException extends Exception {

   public GlPTPeriodTypeAlreadyAssignedException() {
      Debug.print("GlPTPeriodTypeAlreadyAssignedException Constructor");
   }

   public GlPTPeriodTypeAlreadyAssignedException(String msg) {
      super(msg);
      Debug.print("GlPTPeriodTypeAlreadyAssignedException Constructor");
   }
}
