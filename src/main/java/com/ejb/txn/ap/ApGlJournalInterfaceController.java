package com.ejb.txn.ap;

import jakarta.ejb.Local;

@Local
public interface ApGlJournalInterfaceController {

    long executeApGlJriRun(java.util.Date JRI_DT_FRM, java.util.Date JRI_DT_TO, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

}