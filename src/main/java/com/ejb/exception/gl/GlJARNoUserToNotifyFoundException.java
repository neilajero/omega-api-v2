package com.ejb.exception.gl;

import com.util.Debug;

public class GlJARNoUserToNotifyFoundException extends Exception {

   public GlJARNoUserToNotifyFoundException() {
      Debug.print("GlJARNoUserToNotifyFoundException Constructor");
   }

   public GlJARNoUserToNotifyFoundException(String msg) {
      super(msg);
      Debug.print("GlJARNoUserToNotifyFoundException Constructor");
   }
}
