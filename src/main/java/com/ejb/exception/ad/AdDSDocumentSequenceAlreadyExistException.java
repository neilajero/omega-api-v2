package com.ejb.exception.ad;

import com.util.Debug;

public class AdDSDocumentSequenceAlreadyExistException extends Exception {

   public AdDSDocumentSequenceAlreadyExistException() {
      Debug.print("AdDSDocumentSequenceAlreadyExistException Constructor");
   }

   public AdDSDocumentSequenceAlreadyExistException(String msg) {
      super(msg);
      Debug.print("AdDSDocumentSequenceAlreadyExistException Constructor");
   }
}
