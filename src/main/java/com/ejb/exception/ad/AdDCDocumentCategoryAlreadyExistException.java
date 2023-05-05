package com.ejb.exception.ad;

import com.util.Debug;

public class AdDCDocumentCategoryAlreadyExistException extends Exception {

   public AdDCDocumentCategoryAlreadyExistException() {
      Debug.print("AdDCDocumentCategoryAlreadyExistException Constructor");
   }

   public AdDCDocumentCategoryAlreadyExistException(String msg) {
      super(msg);
      Debug.print("AdDCDocumentCategoryAlreadyExistException Constructor");
   }
}
