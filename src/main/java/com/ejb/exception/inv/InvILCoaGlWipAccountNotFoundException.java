package com.ejb.exception.inv;

import com.util.Debug;

public class InvILCoaGlWipAccountNotFoundException extends Exception {

   public InvILCoaGlWipAccountNotFoundException() {
      Debug.print("InvILCoaGlWipAccountNotFoundException Constructor");
   }

   public InvILCoaGlWipAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("InvILCoaGlWipAccountNotFoundException Constructor");
   }
}
