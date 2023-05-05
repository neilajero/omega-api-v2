package com.ejb.exception.ad;

import com.util.Debug;

public class AdPRFCoaGlPettyCashAccountNotFoundException extends Exception {

   public AdPRFCoaGlPettyCashAccountNotFoundException() {
      Debug.print("AdPRFCoaGlPettyCashAccountNotFoundException Constructor");
   }

   public AdPRFCoaGlPettyCashAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("AdPRFCoaGlPettyCashAccountNotFoundException Constructor");
   }
}
