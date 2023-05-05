package com.util;

import jakarta.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EJBHomeFactory {
    private static EJBHomeFactory instance;

    private EJBHomeFactory(InitialContext ctx) throws NamingException {

        InitialContext initialCtx;
        if ((initialCtx = ctx) == null) {
            initialCtx = new InitialContext();
        }
        Map ejbHomes = Collections.synchronizedMap(new HashMap());
    }

    public static synchronized void init() throws NamingException {

        instance = new EJBHomeFactory(null);
    }

    public static EJBHomeFactory getFactory() {

        if (instance == null) {
            try {
                init();
            }
            catch (NamingException e) {
                Debug.print("Fatal error!");
            }
        }
        return instance;
    }

    public static Object lookUpLocalHome(String jndiName, Class localHomeClass) throws NamingException {

        final Object o = getFactory().lookUpLocal(jndiName);
        return o;
    }

    public Object lookUpLocal(String jndiName) {

        String jndiPath = String.format("java:module/%s", jndiName);
        try {
            InitialContext ctx = new InitialContext();
            return ctx.lookup(jndiPath);
        }
        catch (Exception ex) {
            Debug.print("Exception: ConfigurationClass.getInitialContext: " + jndiPath);
            throw new EJBException(ex.getMessage());
        }
    }

}