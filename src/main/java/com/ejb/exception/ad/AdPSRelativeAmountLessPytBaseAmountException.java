package com.ejb.exception.ad;

import com.util.Debug;

public class AdPSRelativeAmountLessPytBaseAmountException extends Exception {

   public AdPSRelativeAmountLessPytBaseAmountException() {
      Debug.print("AdPSRelativeAmountLessPytBaseAmountException Constructor");
   }

   public AdPSRelativeAmountLessPytBaseAmountException(String msg) {
      super(msg);
      Debug.print("AdPSRelativeAmountLessPytBaseAmountException Constructor");
   }
   
}
