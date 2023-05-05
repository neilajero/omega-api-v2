package com.ejb.exception.inv;

import com.util.Debug;

public class InvTagSerialNumberNotFoundException extends Exception {

   public InvTagSerialNumberNotFoundException() {
      Debug.print("InvTagSerialNumberNotFoundException Constructor");
   }

   public InvTagSerialNumberNotFoundException(String msg) {
      super(msg);
      Debug.print("InvTagSerialNumberNotFoundException Constructor");
   }
}
