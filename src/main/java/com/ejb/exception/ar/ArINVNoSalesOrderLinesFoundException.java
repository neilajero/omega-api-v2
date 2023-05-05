package com.ejb.exception.ar;

import com.util.Debug;

public class ArINVNoSalesOrderLinesFoundException extends Exception {

   public ArINVNoSalesOrderLinesFoundException() {
      Debug.print("ArINVNoSalesOrderLinesFoundException Constructor");
   }

   public ArINVNoSalesOrderLinesFoundException(String msg) {
      super(msg);
      Debug.print("ArINVNoSalesOrderLinesFoundException Constructor");
   }
}
