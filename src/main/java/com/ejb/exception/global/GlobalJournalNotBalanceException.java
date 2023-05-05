package com.ejb.exception.global;

import com.util.Debug;

public class GlobalJournalNotBalanceException extends Exception {

   public GlobalJournalNotBalanceException() {
      Debug.print("GlobalJournalNotBalanceException Constructor");
   }

   public GlobalJournalNotBalanceException(String msg) {
      super(msg);
      Debug.print("GlobalJournalNotBalanceException Constructor");
   }
}
