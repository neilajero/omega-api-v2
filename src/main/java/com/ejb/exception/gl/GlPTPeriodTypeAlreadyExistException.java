package com.ejb.exception.gl;

import com.util.Debug;

public class GlPTPeriodTypeAlreadyExistException extends Exception {

   public GlPTPeriodTypeAlreadyExistException() {
      Debug.print("GlPTPeriodTypeAlreadyExistException Constructor");
   }

   public GlPTPeriodTypeAlreadyExistException(String msg) {
      super(msg);
      Debug.print("GlPTPeriodTypeAlreadyExistException Constructor");
   }
}
