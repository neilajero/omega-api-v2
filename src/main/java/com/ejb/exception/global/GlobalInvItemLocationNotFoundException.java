package com.ejb.exception.global;

import com.util.Debug;

public class GlobalInvItemLocationNotFoundException extends Exception {

   public GlobalInvItemLocationNotFoundException() {
      Debug.print("GlobalInvItemLocationNotFoundException Constructor");
   }

   public GlobalInvItemLocationNotFoundException(String msg) {
      super(msg);
      Debug.print("GlobalInvItemLocationNotFoundException Constructor");
   }
}
