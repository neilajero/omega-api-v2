package com.ejb.txnsync.inv;

import jakarta.ejb.Local;

@Local
public interface InvTransferSyncController {
    int setInvTransferAllNewAndVoid(String[] newTransfers, String[] voidTransfers, String BR_BRNCH_CODE, Integer AD_CMPNY);
}