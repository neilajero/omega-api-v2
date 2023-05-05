package com.ejb.exception.gl;

import com.util.Debug;

public class GlJLChartOfAccountNotFoundException extends Exception {

   public GlJLChartOfAccountNotFoundException() {
      Debug.print("GlJLChartOfAccountNotFoundException Constructor");
   }

   public GlJLChartOfAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("GlJLChartOfAccountNotFoundException Constructor");
   }
}
