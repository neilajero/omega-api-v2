package com.ejb.txn.ar;

import jakarta.ejb.Local;

@Local
public interface ArGlJournalInterfaceController {

    long executeArGlJriRun(java.util.Date JRI_DT_FRM, java.util.Date JRI_DT_TO, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

}