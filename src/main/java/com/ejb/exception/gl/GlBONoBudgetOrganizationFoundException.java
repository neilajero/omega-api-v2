package com.ejb.exception.gl;

import com.util.Debug;

public class GlBONoBudgetOrganizationFoundException extends Exception {

   public GlBONoBudgetOrganizationFoundException() {
      Debug.print("GlBONoBudgetOrganizationFoundException Constructor");
   }

   public GlBONoBudgetOrganizationFoundException(String msg) {
      super(msg);
      Debug.print("GlBONoBudgetOrganizationFoundException Constructor");
   }
}
