package com.ejb.exception.gl;

import com.util.Debug;

public class GlJANINoUserToNotifyFoundException extends Exception {

   public GlJANINoUserToNotifyFoundException() {
      Debug.print("GlJANINoUserToNotifyFoundException Constructor");
   }

   public GlJANINoUserToNotifyFoundException(String msg) {
      super(msg);
      Debug.print("GlJANINoUserToNotifyFoundException Constructor");
   }
}
