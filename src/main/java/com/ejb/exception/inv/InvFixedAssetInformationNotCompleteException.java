package com.ejb.exception.inv;

import com.util.Debug;

public class InvFixedAssetInformationNotCompleteException extends Exception {

   public InvFixedAssetInformationNotCompleteException() {
      Debug.print("InvFixedAssetAccountNotFoundException Constructor");
   }

   public InvFixedAssetInformationNotCompleteException(String msg) {
      super(msg);
      Debug.print("InvFixedAssetAccountNotFoundException Constructor");
   }
}
