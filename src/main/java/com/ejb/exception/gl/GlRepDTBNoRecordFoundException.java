package com.ejb.exception.gl;

import com.util.Debug;

public class GlRepDTBNoRecordFoundException extends Exception {

   public GlRepDTBNoRecordFoundException() {
      Debug.print("GlRepDTBNoRecordFoundException Constructor");
   }

   public GlRepDTBNoRecordFoundException(String msg) {
      super(msg);
      Debug.print("GlRepDTBNoRecordFoundException Constructor");
   }
}
