package com.ejb.exception.gen;

import com.util.Debug;

public class GenFlNoGenericFieldFoundException extends Exception {

   public GenFlNoGenericFieldFoundException() {
      Debug.print("GenFlNoGenericFieldFoundException Constructor");
   }

   public GenFlNoGenericFieldFoundException(String msg) {
      super(msg);
      Debug.print("GenFlNoGenericFieldFoundException Constructor");
   }
}
