package com.ejb.exception.ad;

import com.util.Debug;

public class AdPRFCoaGlPosGiftCertificateAccountNotFoundException extends Exception {

   public AdPRFCoaGlPosGiftCertificateAccountNotFoundException() {
      Debug.print("AdPRFCoaGlPosGiftCertificateAccountNotFoundException Constructor");
   }

   public AdPRFCoaGlPosGiftCertificateAccountNotFoundException(String msg) {
      super(msg);
      Debug.print("AdPRFCoaGlPosGiftCertificateAccountNotFoundException Constructor");
   }
}
