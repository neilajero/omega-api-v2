package com.ejb.exception.gl;

import com.util.Debug;

public class GlORGNoOrganizationFoundException extends Exception {

   public GlORGNoOrganizationFoundException() {
      Debug.print("GlORGNoOrganizationFoundException Constructor");
   }

   public GlORGNoOrganizationFoundException(String msg) {
      super(msg);
      Debug.print("GlORGNoOrganizationFoundException Constructor");
   }
}
