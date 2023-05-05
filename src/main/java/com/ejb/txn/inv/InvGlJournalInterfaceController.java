
package com.ejb.txn.inv;

import jakarta.ejb.Local;


@Local
public interface InvGlJournalInterfaceController
   
{

   long executeInvGlJriRun(java.util.Date JRI_DT_FRM, java.util.Date JRI_DT_TO, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY)
           ;

}