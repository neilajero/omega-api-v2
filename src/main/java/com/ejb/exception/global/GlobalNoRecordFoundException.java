package com.ejb.exception.global;

import com.util.Debug;

public class GlobalNoRecordFoundException extends Exception {

   public GlobalNoRecordFoundException() {
      Debug.print("GlobalNoRecordFoundException Constructor");
   }

   public GlobalNoRecordFoundException(String msg) {
      super(msg);
      Debug.print("GlobalNoRecordFoundException Constructor");
   }
}
