package com.ejb.exception.global;

import com.util.Debug;

public class GlobalSegmentValueInvalidException extends Exception {

   public GlobalSegmentValueInvalidException() {
      Debug.print("GlobalSegmentValueInvalidException Constructor");
   }

   public GlobalSegmentValueInvalidException(String msg) {
      super(msg);
      Debug.print("GlobalSegmentValueInvalidException Constructor");
   }
}
