package com.ejb.exception.gl;

import com.util.Debug;

public class GlORGNoOrganizationCodeFoundException extends Exception {

   public GlORGNoOrganizationCodeFoundException() {
      Debug.print("GlORGNoOrganizationCodeFoundException Constructor");
   }

   public GlORGNoOrganizationCodeFoundException(String msg) {
      super(msg);
      Debug.print("GlORGNoOrganizationCodeFoundException Constructor");
   }
}
