package com.ejb.exception.gl;

import com.util.Debug;

public class GlORGOrganizationAlreadyAssignedException extends Exception {

   public GlORGOrganizationAlreadyAssignedException() {
      Debug.print("GlORGOrganizationAlreadyAssignedException Constructor");
   }

   public GlORGOrganizationAlreadyAssignedException(String msg) {
      super(msg);
      Debug.print("GlORGOrganizationAlreadyAssignedException Constructor");
   }
}
