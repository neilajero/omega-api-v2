package com.ejb.txn.cm;

import jakarta.ejb.Local;

@Local
public interface CmGlJournalInterfaceController {

    long executeCmGlJriImport(java.util.Date JRI_DT_FRM, java.util.Date JRI_DT_TO, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

}