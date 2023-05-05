package com.ejb.exception.global;

import com.util.Debug;

public class GlobalInvItemNotAssemblyException extends Exception {

   public GlobalInvItemNotAssemblyException() {
      Debug.print("GlobalInvItemNotAssemblyException Constructor");
   }

   public GlobalInvItemNotAssemblyException(String msg) {
      super(msg);
      Debug.print("GlobalInvItemNotAssemblyException Constructor");
   }
}
