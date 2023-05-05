package com.ejb.exception.gl;

import com.util.Debug;

public class GlJSJournalSourceAlreadyExistException extends Exception {

   public GlJSJournalSourceAlreadyExistException() {
      Debug.print("GlJSJournalSourceAlreadyExistException Constructor");
   }

   public GlJSJournalSourceAlreadyExistException(String msg) {
      super(msg);
      Debug.print("GlJSJournalSourceAlreadyExistException Constructor");
   }
}
