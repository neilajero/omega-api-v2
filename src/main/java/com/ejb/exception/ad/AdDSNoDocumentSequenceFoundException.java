package com.ejb.exception.ad;

import com.util.Debug;

public class AdDSNoDocumentSequenceFoundException extends Exception {

   public AdDSNoDocumentSequenceFoundException() {
      Debug.print("AdDSNoDocumentSequenceFoundException Constructor");
   }

   public AdDSNoDocumentSequenceFoundException(String msg) {
      super(msg);
      Debug.print("AdDSNoDocumentSequenceFoundException Constructor");
   }
}
