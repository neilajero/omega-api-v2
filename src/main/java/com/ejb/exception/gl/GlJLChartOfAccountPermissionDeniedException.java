package com.ejb.exception.gl;

import com.util.Debug;

public class GlJLChartOfAccountPermissionDeniedException extends Exception {

   public GlJLChartOfAccountPermissionDeniedException() {
      Debug.print("GlJLChartOfAccountPermissionDeniedException Constructor");
   }

   public GlJLChartOfAccountPermissionDeniedException(String msg) {
      super(msg);
      Debug.print("GlJLChartOfAccountPermissionDeniedException Constructor");
   }
}
