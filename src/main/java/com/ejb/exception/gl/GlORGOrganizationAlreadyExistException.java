package com.ejb.exception.gl;

import com.util.Debug;

public class GlORGOrganizationAlreadyExistException extends Exception {

   public GlORGOrganizationAlreadyExistException() {
      Debug.print("GlORGOrganizationAlreadyExistException Constructor");
   }

   public GlORGOrganizationAlreadyExistException(String msg) {
      super(msg);
      Debug.print("GlORGOrganizationAlreadyExistException Constructor");
   }
}
