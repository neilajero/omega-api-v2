package com.ejb.exception.ad;

import com.util.Debug;

public class AdBACoaGlAccountReceiptNotFoundException extends Exception {

   public AdBACoaGlAccountReceiptNotFoundException() {
      Debug.print("AdBACoaGlAccountReceiptNotFoundException Constructor");
   }

   public AdBACoaGlAccountReceiptNotFoundException(String msg) {
      super(msg);
      Debug.print("AdBACoaGlAccountReceiptNotFoundException Constructor");
   }
}
