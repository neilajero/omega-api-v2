package com.ejb.exception.ad;

import com.util.Debug;

public class AdDSDocumentSequenceAlreadyDeletedException extends Exception {

   public AdDSDocumentSequenceAlreadyDeletedException() {
      Debug.print("AdDSDocumentSequenceAlreadyDeletedException Constructor");
   }

   public AdDSDocumentSequenceAlreadyDeletedException(String msg) {
      super(msg);
      Debug.print("AdDSDocumentSequenceAlreadyDeletedException Constructor");
   }
}
