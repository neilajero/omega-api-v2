package com.ejb.exception.global;

import com.util.Debug;

public class GlobalMiscInfoIsRequiredException extends Exception {

   public GlobalMiscInfoIsRequiredException() {
      Debug.print("Misc Info is Required");
   }

   public GlobalMiscInfoIsRequiredException(String msg) {
      super(msg);
      Debug.print("Misc Info is Required");
   }
}
