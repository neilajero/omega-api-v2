package com.ejb.exception.gl;

import com.util.Debug;

public class GlFrgCALRowAlreadyHasAccountAssignmentException extends Exception {

    public GlFrgCALRowAlreadyHasAccountAssignmentException() {
      Debug.print("GlFrgCALRowAlreadyHasAccountAssignmentException Constructor");
   }

    public GlFrgCALRowAlreadyHasAccountAssignmentException(String msg) {
      super(msg);
      Debug.print("GlFrgCALRowAlreadyHasAccountAssignmentException Constructor");
   }
}
