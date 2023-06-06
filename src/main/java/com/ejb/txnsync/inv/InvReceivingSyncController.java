package com.ejb.txnsync.inv;

import jakarta.ejb.Local;

@Local
public interface InvReceivingSyncController {

    int setInvReceivingAllNewAndVoid(String[] newRR, String[] voidRR, String BR_BRNCH_CODE, Integer AD_CMPNY, String INV_LOCATION, String CASHIER);
}