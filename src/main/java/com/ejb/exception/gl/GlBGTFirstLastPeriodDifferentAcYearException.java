package com.ejb.exception.gl;

import com.util.Debug;

public class GlBGTFirstLastPeriodDifferentAcYearException extends Exception {

   public GlBGTFirstLastPeriodDifferentAcYearException() {
      Debug.print("GlBGTFirstLastPeriodDifferentAcYearException Constructor");
   }

   public GlBGTFirstLastPeriodDifferentAcYearException(String msg) {
      super(msg);
      Debug.print("GlBGTFirstLastPeriodDifferentAcYearException Constructor");
   }
}
