package com.ejb.exception.gl;

import com.util.Debug;

public class GlBGTNoBudgetFoundException extends Exception {

   public GlBGTNoBudgetFoundException() {
      Debug.print("GlBGTNoBudgetFoundException Constructor");
   }

   public GlBGTNoBudgetFoundException(String msg) {
      super(msg);
      Debug.print("GlBGTNoBudgetFoundException Constructor");
   }
}
