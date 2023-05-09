package com.ejb.exception.ar;

import com.util.Debug;

public class ArInvoiceStandardMemolineDoesNotExist  extends Exception {

    public ArInvoiceStandardMemolineDoesNotExist() {
        Debug.print("ArInvoiceStandardMemolineDoesNotExist Constructor");
    }

    public ArInvoiceStandardMemolineDoesNotExist(String msg) {
        super(msg);
        Debug.print("ArInvoiceStandardMemolineDoesNotExist Constructor");
    }
}