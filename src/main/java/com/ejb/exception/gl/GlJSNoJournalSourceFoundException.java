package com.ejb.exception.gl;

import com.util.Debug;

public class GlJSNoJournalSourceFoundException extends Exception {

   public GlJSNoJournalSourceFoundException() {
      Debug.print("GlJSNoJournalSourceFoundException Constructor");
   }

   public GlJSNoJournalSourceFoundException(String msg) {
      super(msg);
      Debug.print("GlJSNoJournalSourceFoundException Constructor");
   }
}
