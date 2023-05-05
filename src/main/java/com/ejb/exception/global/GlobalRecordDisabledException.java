package com.ejb.exception.global;

import com.util.Debug;

public class GlobalRecordDisabledException extends Exception {

    public GlobalRecordDisabledException() {
      Debug.print("GlobalRecordDisabledException Constructor");
   }

    public GlobalRecordDisabledException(String msg) {
      super(msg);
      Debug.print("GlobalRecordDisabledException Constructor");
   }
}
