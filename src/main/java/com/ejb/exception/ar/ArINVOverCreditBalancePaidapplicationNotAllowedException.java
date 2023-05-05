package com.ejb.exception.ar;

import com.util.Debug;

public class ArINVOverCreditBalancePaidapplicationNotAllowedException extends Exception {

   public ArINVOverCreditBalancePaidapplicationNotAllowedException() {
      Debug.print("ArINVOverCreditBalancePaidapplicationNotAllowedException Constructor");
   }

   public ArINVOverCreditBalancePaidapplicationNotAllowedException(String msg) {
      super(msg);
      Debug.print("ArINVOverCreditBalancePaidapplicationNotAllowedException Constructor");
   }
}
