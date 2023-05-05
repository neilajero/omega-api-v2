package com.ejb.exception.gl;

import com.util.Debug;

public class GlACVPeriodPrefixNotUniqueException extends Exception {

   public GlACVPeriodPrefixNotUniqueException() {
      Debug.print("GlACVPeriodPrefixNotUniqueException Constructor");
   }

   public GlACVPeriodPrefixNotUniqueException(String msg) {
      super(msg);
      Debug.print("GlACVPeriodPrefixNotUniqueException Constructor");
   }
}
