package com.ejb.exception.global;

import com.util.Debug;

public class GlobalRecordAlreadyDeletedException extends Exception {

   public GlobalRecordAlreadyDeletedException() {
      Debug.print("GlobalRecordAlreadyDeletedException Constructor");
   }

   public GlobalRecordAlreadyDeletedException(String msg) {
      super(msg);
      Debug.print("GlobalRecordAlreadyDeletedException Constructor");
   }
}
