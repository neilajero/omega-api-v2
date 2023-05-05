package com.ejb.exception.ad;

import com.util.Debug;

public class AdUSRPasswordInvalidException extends Exception {

   public AdUSRPasswordInvalidException() {
      Debug.print("AdUSRPasswordInvalidException Constructor");
   }

   public AdUSRPasswordInvalidException(String msg) {
      super(msg);
      Debug.print("AdUSRPasswordInvalidException Constructor");
   }
}
