package com.util;

import jakarta.annotation.Resource;
import jakarta.ejb.EJBContext;
import jakarta.persistence.Inheritance;

@Inheritance
public class EJBContextClass {
	@Resource
    public EJBContext ctx;

	public EJBContext getSessionContext() {
		return ctx;
	}

}