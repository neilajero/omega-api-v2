package com.ejb.exception.inv;

import com.util.Debug;

public class InvATRAssemblyQtyGreaterThanAvailableQtyException extends Exception {

   public InvATRAssemblyQtyGreaterThanAvailableQtyException() {
      Debug.print("InvATRAssemblyQtyGreaterThanAvailableQtyException Constructor");
   }

   public InvATRAssemblyQtyGreaterThanAvailableQtyException(String msg) {
      super(msg);
      Debug.print("InvATRAssemblyQtyGreaterThanAvailableQtyException Constructor");
   }
}
