package com.ejb.exception.gl;

import com.util.Debug;

public class GlCOANoChartOfAccountFoundException extends Exception {

   public GlCOANoChartOfAccountFoundException() {
      Debug.print("GlCOANoChartOfAccountFoundException Constructor");
   }

   public GlCOANoChartOfAccountFoundException(String msg) {
      super(msg);
      Debug.print("GlCOANoChartOfAccountFoundException Constructor");
   }
}
