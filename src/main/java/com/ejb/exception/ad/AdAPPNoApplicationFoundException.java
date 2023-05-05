package com.ejb.exception.ad;

import com.util.Debug;

public class AdAPPNoApplicationFoundException extends Exception {

   public AdAPPNoApplicationFoundException() {
      Debug.print("AdAPPNoApplicationFoundException Constructor");
   }

   public AdAPPNoApplicationFoundException(String msg) {
      super(msg);
      Debug.print("AdAPPNoApplicationFoundException Constructor");
   }
}
