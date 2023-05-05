package com.ejb.exception.gl;

import com.util.Debug;

public class GlJSJournalSourceAlreadyDeletedException  extends Exception {

   public GlJSJournalSourceAlreadyDeletedException () {
      Debug.print("GlJSJournalSourceAlreadyDeletedException  Constructor");
   }

   public GlJSJournalSourceAlreadyDeletedException (String msg) {
      super(msg);
      Debug.print("GlJSJournalSourceAlreadyDeletedException  Constructor");
   }
}
