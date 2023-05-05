package com.ejb.exception.global;

import com.util.Debug;

public class GlobalRecordAlreadyAssignedException extends Exception {

   public GlobalRecordAlreadyAssignedException() {
      Debug.print("GlobalRecordAlreadyAssignedException Constructor");
   }

   public GlobalRecordAlreadyAssignedException(String msg) {
      super(msg);
      Debug.print("GlobalRecordAlreadyAssignedException Constructor");
   }
}
