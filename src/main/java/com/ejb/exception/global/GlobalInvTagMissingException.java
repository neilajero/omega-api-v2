package com.ejb.exception.global;

import com.util.Debug;

public class GlobalInvTagMissingException extends Exception {

   public GlobalInvTagMissingException() {
      Debug.print("GlobalInvTagMissingException Constructor");
   }

   public GlobalInvTagMissingException(String msg) {
      super(msg);
      Debug.print("GlobalInvTagMissingException Constructor");
   }
}
