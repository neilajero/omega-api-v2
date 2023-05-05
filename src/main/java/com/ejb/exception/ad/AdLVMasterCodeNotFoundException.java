package com.ejb.exception.ad;

import com.util.Debug;

public class AdLVMasterCodeNotFoundException extends Exception {

   public AdLVMasterCodeNotFoundException() {
      Debug.print("AdLVMasterCodeNotFoundException Constructor");
   }

   public AdLVMasterCodeNotFoundException(String msg) {
      super(msg);
      Debug.print("AdLVMasterCodeNotFoundException Constructor");
   }
}
