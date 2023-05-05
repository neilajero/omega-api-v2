package com.ejb.exception.ad;

import com.util.Debug;

public class AdDCNoDocumentCategoryFoundException extends Exception {

   public AdDCNoDocumentCategoryFoundException() {
      Debug.print("AdDCNoDocumentCategoryFoundException Constructor");
   }

   public AdDCNoDocumentCategoryFoundException(String msg) {
      super(msg);
      Debug.print("AdDCNoDocumentCategoryFoundException Constructor");
   }
}
