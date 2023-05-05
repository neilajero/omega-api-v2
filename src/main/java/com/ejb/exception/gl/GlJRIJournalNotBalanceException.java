package com.ejb.exception.gl;

import com.util.*;

public class GlJRIJournalNotBalanceException extends Exception {

   public GlJRIJournalNotBalanceException() {
      Debug.print("GlJRIJournalNotBalanceException Constructor");
   }

   public GlJRIJournalNotBalanceException(String msg) {
      super(msg);
      Debug.print("GlJRIJournalNotBalanceException Constructor");
   }
}
