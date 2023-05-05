package com.ejb.exception.inv;

import com.util.Debug;

public class InvTagSerialNumberAlreadyExistException extends Exception {

   public InvTagSerialNumberAlreadyExistException() {
      Debug.print("InvTagSerialNumberAlreadyExistException Constructor");
   }

   public InvTagSerialNumberAlreadyExistException(String msg) {
      super(msg);
      Debug.print("InvTagSerialNumberAlreadyExistException Constructor");
   }
}
