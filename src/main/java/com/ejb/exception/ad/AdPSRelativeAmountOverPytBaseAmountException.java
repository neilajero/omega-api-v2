package com.ejb.exception.ad;

import com.util.Debug;

public class AdPSRelativeAmountOverPytBaseAmountException extends Exception {

   public AdPSRelativeAmountOverPytBaseAmountException() {
      Debug.print("AdPSRelativeAmountOverPytBaseAmountException Constructor");
   }

   public AdPSRelativeAmountOverPytBaseAmountException(String msg) {
      super(msg);
      Debug.print("AdPSRelativeAmountOverPytBaseAmountException Constructor");
   }
   
}
