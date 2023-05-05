package com.ejb.exception.ad;

import com.util.Debug;

public class AdDSADocumentSequenceAssignmentAlreadyAssignedException extends Exception {

   public AdDSADocumentSequenceAssignmentAlreadyAssignedException() {
      Debug.print("AdDSADocumentSequenceAssignmentAlreadyAssignedException Constructor");
   }

   public AdDSADocumentSequenceAssignmentAlreadyAssignedException(String msg) {
      super(msg);
      Debug.print("AdDSADocumentSequenceAssignmentAlreadyAssignedException Constructor");
   }
}
