package com.ejb.exception.gl;

import com.util.Debug;

public class GlRepDISNoRecordFoundException extends Exception {

   public GlRepDISNoRecordFoundException() {
      Debug.print("GlRepDISNoRecordFoundException Constructor");
   }

   public GlRepDISNoRecordFoundException(String msg) {
      super(msg);
      Debug.print("GlRepDISNoRecordFoundException Constructor");
   }
}
