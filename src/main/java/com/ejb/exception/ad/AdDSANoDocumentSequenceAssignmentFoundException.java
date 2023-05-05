package com.ejb.exception.ad;

import com.util.Debug;

public class AdDSANoDocumentSequenceAssignmentFoundException extends Exception {

   public AdDSANoDocumentSequenceAssignmentFoundException() {
      Debug.print("AdDSANoDocumentSequenceAssignmentFoundException Constructor");
   }

   public AdDSANoDocumentSequenceAssignmentFoundException(String msg) {
      super(msg);
      Debug.print("AdDSANoDocumentSequenceAssignmentFoundException Constructor");
   }
}
