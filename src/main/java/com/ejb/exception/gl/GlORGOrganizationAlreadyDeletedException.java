package com.ejb.exception.gl;

import com.util.Debug;

public class GlORGOrganizationAlreadyDeletedException extends Exception {

   public GlORGOrganizationAlreadyDeletedException() {
      Debug.print("GlORGOrganizationAlreadyDeletedException Constructor");
   }

   public GlORGOrganizationAlreadyDeletedException(String msg) {
      super(msg);
      Debug.print("GlORGOrganizationAlreadyDeletedException Constructor");
   }
}
