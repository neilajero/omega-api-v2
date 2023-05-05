package com.ejb.exception.inv;

import com.util.Debug;

public class InvFixedAssetAccountNotFoundException extends Exception {

   public InvFixedAssetAccountNotFoundException() {
      Debug.print("InvFixedAssetAccountNotFoundException Constructor");
   }

   public InvFixedAssetAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("InvFixedAssetAccountNotFoundException Constructor");
   }
}
