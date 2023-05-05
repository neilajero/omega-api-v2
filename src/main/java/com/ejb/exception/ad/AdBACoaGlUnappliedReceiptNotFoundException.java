package com.ejb.exception.ad;

import com.util.Debug;

public class AdBACoaGlUnappliedReceiptNotFoundException extends Exception {

   public AdBACoaGlUnappliedReceiptNotFoundException() {
      Debug.print("AdBACoaGlUnappliedReceiptNotFoundException Constructor");
   }

   public AdBACoaGlUnappliedReceiptNotFoundException(String msg) {
      super(msg);
      Debug.print("AdBACoaGlUnappliedReceiptNotFoundException Constructor");
   }
}
