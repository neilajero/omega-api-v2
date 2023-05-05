package com.ejb.exception.ad;

import com.util.Debug;

public class AdLVNoLookUpValueFoundException extends Exception {

   public AdLVNoLookUpValueFoundException() {
      Debug.print("AdLVNoLookUpValueFoundException Constructor");
   }

   public AdLVNoLookUpValueFoundException(String msg) {
      super(msg);
      Debug.print("AdLVNoLookUpValueFoundException Constructor");
   }
}
