package com.ejb.exception.inv;

import com.util.Debug;

public class InvLocFixedAssetException extends Exception {

   public InvLocFixedAssetException() {
      Debug.print("InvLocFixedAssetException Constructor");
   }

   public InvLocFixedAssetException(String msg) {
      super(msg);
      Debug.print("InvLocFixedAssetException Constructor");
   }
}
