package com.ejb.exception.global;

import com.util.Debug;

public class GlobalInsufficientBudgetQuantityException extends Exception {

   public GlobalInsufficientBudgetQuantityException() {
      Debug.print("GlobalInsufficientBudgetQuantityException Constructor");
   }

   public GlobalInsufficientBudgetQuantityException(String msg) {
      super(msg);
      Debug.print("GlobalInsufficientBudgetQuantityException Constructor");
   }
}
