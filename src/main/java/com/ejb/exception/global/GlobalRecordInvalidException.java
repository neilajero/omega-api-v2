package com.ejb.exception.global;

import com.util.Debug;

public class GlobalRecordInvalidException extends Exception {

   public GlobalRecordInvalidException() {
      Debug.print("GlobalRecordInvalidException Constructor");
   }

   public GlobalRecordInvalidException(String msg) {
      super(msg);
      Debug.print("GlobalRecordInvalidException Constructor");
   }
}
