package com.ejb.exception.ap;

import com.util.Debug;

public class ApRINoPurchaseOrderLinesFoundException extends Exception {

   public ApRINoPurchaseOrderLinesFoundException() {
      Debug.print("ApRINoPurchaseOrderLinesFoundException Constructor");
   }

   public ApRINoPurchaseOrderLinesFoundException(String msg) {
      super(msg);
      Debug.print("ApRINoPurchaseOrderLinesFoundException Constructor");
   }
}
