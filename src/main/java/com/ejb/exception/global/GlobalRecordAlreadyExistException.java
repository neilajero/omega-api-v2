package com.ejb.exception.global;

import com.util.Debug;

public class GlobalRecordAlreadyExistException extends Exception {

    public GlobalRecordAlreadyExistException() {
      Debug.print("GlobalRecordAlreadyExistException Constructor");
   }

    public GlobalRecordAlreadyExistException(String msg) {
      super(msg);
      Debug.print("GlobalRecordAlreadyExistException Constructor");
   }
}
