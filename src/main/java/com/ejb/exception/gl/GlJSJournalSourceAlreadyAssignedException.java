package com.ejb.exception.gl;

import com.util.Debug;

public class GlJSJournalSourceAlreadyAssignedException extends Exception {

   public GlJSJournalSourceAlreadyAssignedException() {
      Debug.print("GlJSJournalSourceAlreadyAssignedException Constructor");
   }

   public GlJSJournalSourceAlreadyAssignedException(String msg) {
      super(msg);
      Debug.print("GlJSJournalSourceAlreadyAssignedException Constructor");
   }
}
