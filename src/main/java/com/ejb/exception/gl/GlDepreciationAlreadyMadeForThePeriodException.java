package com.ejb.exception.gl;

import com.util.Debug;

public class GlDepreciationAlreadyMadeForThePeriodException extends Exception {

   public GlDepreciationAlreadyMadeForThePeriodException() {
      Debug.print("GlCOANoChartOfAccountFoundException Constructor");
   }

   public GlDepreciationAlreadyMadeForThePeriodException(String msg) {
      super(msg);
      Debug.print("GlCOANoChartOfAccountFoundException Constructor");
   }
}
