package com.ejb.exception.ad;

import com.util.Debug;

public class AdDSADocumentSequenceAssignmentAlreadyDeletedException extends Exception {

   public AdDSADocumentSequenceAssignmentAlreadyDeletedException() {
      Debug.print("AdDSADocumentSequenceAssignmentAlreadyDeletedException Constructor");
   }

   public AdDSADocumentSequenceAssignmentAlreadyDeletedException(String msg) {
      super(msg);
      Debug.print("AdDSADocumentSequenceAssignmentAlreadyDeletedException Constructor");
   }
}
