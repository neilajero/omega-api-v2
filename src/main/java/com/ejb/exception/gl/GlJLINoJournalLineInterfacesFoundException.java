package com.ejb.exception.gl;

import com.util.Debug;

public class GlJLINoJournalLineInterfacesFoundException extends Exception {

   public GlJLINoJournalLineInterfacesFoundException() {
      Debug.print("GlJLINoJournalLineInterfacesFoundException Constructor");
   }

   public GlJLINoJournalLineInterfacesFoundException(String msg) {
      super(msg);
      Debug.print("GlJLINoJournalLineInterfacesFoundException Constructor");
   }
}
