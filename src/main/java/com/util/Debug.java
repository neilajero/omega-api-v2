package com.util;

import org.jboss.logging.Logger;

public final class Debug {

    public static final boolean debuggingOn = true;
    public static Logger log = Logger.getLogger("com.ejb.txn");

    public static void print(String msg) {
        if (debuggingOn) {
            log.info(msg);
        }
    }

    public static void print(String msg, String objectName) {
        if (debuggingOn) {
            log = Logger.getLogger(objectName);
            log.info(msg);
        }
    }

    public static void print(String msg, Object object) {
        if (debuggingOn) {
            System.err.print("Debug: " + msg);
            System.err.print("       " + object.getClass().getName());
        }
    }

    public static void printStackTrace(Exception ex) {
        if (debuggingOn) {
            ex.printStackTrace();
        }
    }
}