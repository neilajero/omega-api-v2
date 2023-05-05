package com.ejb.txn.ap;

import com.ejb.exception.ap.ApSCCoaGlExpenseAccountNotFoundException;
import com.ejb.exception.ap.ApSCCoaGlPayableAccountNotFoundException;
import com.ejb.exception.global.GlobalNameAndAddressAlreadyExistsException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.ad.AdResponsibilityDetails;
import com.util.mod.ap.ApModSupplierClassDetails;
import com.util.mod.ap.ApModSupplierDetails;
import com.util.mod.ap.ApModSupplierTypeDetails;

import jakarta.ejb.Local;


@Local
public interface ApSupplierEntryController {

    java.util.ArrayList getApStAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getArCstAll(java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApPytAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getApScAll(java.lang.Integer AD_CMPNY);

    byte getPrfApAutoGenerateSupplierCode(java.lang.Integer AD_CMPNY);

    java.lang.String getPrfApNextSupplierCode(java.lang.Integer AD_CMPNY);

    void saveApSplEntry(ApModSupplierDetails details, java.lang.String ST_NM, java.lang.String PYT_NM, java.lang.String SC_NM, java.lang.String SPL_COA_GL_PYBL_ACCNT, java.lang.String SPL_COA_GL_EXPNS_ACCNT, java.lang.String BA_NM, java.lang.String RS_NM, java.util.ArrayList branchList, java.lang.String LIT_NM, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalNameAndAddressAlreadyExistsException, ApSCCoaGlPayableAccountNotFoundException, ApSCCoaGlExpenseAccountNotFoundException;

    ApModSupplierDetails getApSplBySplCode(java.lang.Integer SPL_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAdBaAll(java.lang.Integer AD_CMPNY);

    ApModSupplierClassDetails getApScByScName(java.lang.String SC_NM, java.lang.Integer AD_CMPNY);

    ApModSupplierTypeDetails getApStByStName(java.lang.String ST_NM, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdBrAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAdBrSplAll(java.lang.Integer BSPL_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdResponsibilityDetails getAdRsByRsCode(java.lang.Integer RS_CODE) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAdBrSplBySplCode(java.lang.Integer SPL_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getInvLitAll(java.lang.Integer AD_CMPNY);

}