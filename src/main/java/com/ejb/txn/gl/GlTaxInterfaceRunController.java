package com.ejb.txn.gl;

import jakarta.ejb.Local;

@Local
public interface GlTaxInterfaceRunController {

    void runTaxInterface(java.lang.String DOC_TYP, java.util.Date DT_FRM, java.util.Date DT_TO, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

}