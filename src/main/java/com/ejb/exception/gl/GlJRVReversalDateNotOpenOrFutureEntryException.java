package com.ejb.exception.gl;

import com.util.Debug;

public class GlJRVReversalDateNotOpenOrFutureEntryException extends Exception {

   public GlJRVReversalDateNotOpenOrFutureEntryException() { 
      Debug.print("GlJRVReversalDateNotOpenOrFutureEntryException Constructor");
   }

   public GlJRVReversalDateNotOpenOrFutureEntryException(String msg) {
      super(msg);
      Debug.print("GlJRVReversalDateNotOpenOrFutureEntryException Constructor");
   }
}
