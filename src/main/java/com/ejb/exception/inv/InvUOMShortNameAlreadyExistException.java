package com.ejb.exception.inv;

import com.util.Debug;

public class InvUOMShortNameAlreadyExistException extends Exception {

   public InvUOMShortNameAlreadyExistException() {
      Debug.print("InvUOMShortNameAlreadyExistException Constructor");
   }

   public InvUOMShortNameAlreadyExistException(String msg) {
      super(msg);
      Debug.print("InvUOMShortNameAlreadyExistException Constructor");
   }
}
