package com.ejb.exception.ad;

import com.util.Debug;

public class AdDSDocumentSequenceAlreadyAssignedException extends Exception {

   public AdDSDocumentSequenceAlreadyAssignedException() {
      Debug.print("AdDSDocumentSequenceAlreadyAssignedException Constructor");
   }

   public AdDSDocumentSequenceAlreadyAssignedException(String msg) {
      super(msg);
      Debug.print("AdDSDocumentSequenceAlreadyAssignedException Constructor");
   }
}
