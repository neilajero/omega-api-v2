package com.ejb.exception.global;

import com.util.Debug;

public class GlobalSendEmailMessageException extends Exception {

   public GlobalSendEmailMessageException() {
      Debug.print("Invalid Date");
   }

   public GlobalSendEmailMessageException(String msg) {
      super(msg);
      Debug.print("Error send email");
   }
}
