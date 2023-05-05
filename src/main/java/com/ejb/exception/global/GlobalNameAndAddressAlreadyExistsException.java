package com.ejb.exception.global;

import com.util.Debug;

public class GlobalNameAndAddressAlreadyExistsException extends Exception {

    public GlobalNameAndAddressAlreadyExistsException() {
      Debug.print("GlobalNameAndAddressAlreadyExistsException Constructor");
   }

    public GlobalNameAndAddressAlreadyExistsException(String msg) {
      super(msg);
      Debug.print("GlobalNameAndAddressAlreadyExistsException Constructor");
   }
}
