package com.ejb.exception.ad;

import com.util.Debug;

public class AdBACoaGlInterestAccountNotFoundException extends Exception {

   public AdBACoaGlInterestAccountNotFoundException() {
      Debug.print("AdBACoaGlInterestAccountNotFoundException Constructor");
   }

   public AdBACoaGlInterestAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("AdBACoaGlInterestAccountNotFoundException Constructor");
   }
}
