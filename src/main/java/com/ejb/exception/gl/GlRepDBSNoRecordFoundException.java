package com.ejb.exception.gl;

import com.util.Debug;

public class GlRepDBSNoRecordFoundException extends Exception {

   public GlRepDBSNoRecordFoundException() {
      Debug.print("GlRepDBSNoRecordFoundException Constructor");
   }

   public GlRepDBSNoRecordFoundException(String msg) {
      super(msg);
      Debug.print("GlRepDBSNoRecordFoundException Constructor");
   }
}
