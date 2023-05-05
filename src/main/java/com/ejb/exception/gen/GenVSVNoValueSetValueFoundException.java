package com.ejb.exception.gen;

import com.util.Debug;

public class GenVSVNoValueSetValueFoundException extends Exception {

   public GenVSVNoValueSetValueFoundException() {
      Debug.print("GenVSVNoValueSetValueFoundException Constructor");
   }

   public GenVSVNoValueSetValueFoundException(String msg) {
      super(msg);
      Debug.print("GenVSVNoValueSetValueFoundException Constructor");
   }
}
