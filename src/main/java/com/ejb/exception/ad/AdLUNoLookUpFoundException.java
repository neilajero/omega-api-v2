package com.ejb.exception.ad;

import com.util.Debug;

public class AdLUNoLookUpFoundException extends Exception {

   public AdLUNoLookUpFoundException() {
      Debug.print("AdLUNoLookUpFoundException Constructor");
   }

   public AdLUNoLookUpFoundException(String msg) {
      super(msg);
      Debug.print("AdLUNoLookUpFoundException Constructor");
   }
}
