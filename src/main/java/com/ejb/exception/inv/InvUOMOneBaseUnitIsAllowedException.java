package com.ejb.exception.inv;

import com.util.Debug;

public class InvUOMOneBaseUnitIsAllowedException extends Exception {

   public InvUOMOneBaseUnitIsAllowedException() {
      Debug.print("InvUOMOneBaseUnitIsAllowedException Constructor");
   }

   public InvUOMOneBaseUnitIsAllowedException(String msg) {
      super(msg);
      Debug.print("InvUOMOneBaseUnitIsAllowedException Constructor");
   }
}
