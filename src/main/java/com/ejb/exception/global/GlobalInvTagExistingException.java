package com.ejb.exception.global;

import com.util.Debug;

public class GlobalInvTagExistingException extends Exception {

   public GlobalInvTagExistingException() {
      Debug.print("GlobalInvTagExistingException Constructor");
   }

   public GlobalInvTagExistingException(String msg) {
      super(msg);
      Debug.print("GlobalInvTagExistingException Constructor");
   }
}
