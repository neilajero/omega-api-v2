package com.ejb.exception.gl;

import com.util.Debug;

public class GlRepBGTPeriodOutOfRangeException extends Exception {

   public GlRepBGTPeriodOutOfRangeException() {
      Debug.print("GlRepBGTPeriodOutOfRangeException Constructor");
   }

   public GlRepBGTPeriodOutOfRangeException(String msg) {
      super(msg);
      Debug.print("GlRepBGTPeriodOutOfRangeException Constructor");
   }
}
