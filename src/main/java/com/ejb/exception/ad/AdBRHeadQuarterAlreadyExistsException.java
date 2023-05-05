package com.ejb.exception.ad;

import com.util.Debug;

public class AdBRHeadQuarterAlreadyExistsException extends Exception {

   public AdBRHeadQuarterAlreadyExistsException() {
      Debug.print("AdBRHeadQuarterAlreadyExistsException Constructor");
   }

   public AdBRHeadQuarterAlreadyExistsException(String msg) {
      super(msg);
      Debug.print("AdBRHeadQuarterAlreadyExistsException Constructor");
   }
}
