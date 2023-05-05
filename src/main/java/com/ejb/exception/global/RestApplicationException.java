package com.ejb.exception.global;

import java.io.Serializable;

public class RestApplicationException extends Exception implements Serializable{
	
	private static final long serialVersionUID = 1L;
 
    public RestApplicationException() {
        super();
    }
    public RestApplicationException(String msg)   {
        super(msg);
    }
    public RestApplicationException(String msg, Exception e)  {
        super(msg, e);
    }


}
