package com.ejb.txn.ar;

import com.ejb.exception.ar.*;
import com.ejb.exception.global.*;
import com.util.mod.ar.ArModCustomerClassDetails;
import com.util.mod.ar.ArModCustomerDetails;
import com.util.mod.ar.ArModCustomerTypeDetails;

import jakarta.ejb.Local;

@Local
public interface ArCustomerEntryController {

    byte getAdPrfApUseSupplierPulldown(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdApprovalNotifiedUsersByCstCode(java.lang.Integer CST_CODE, java.lang.Integer AD_CMPNY);

    byte getPrfValidateCustomerEmail(java.lang.Integer AD_CMPNY);

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

    java.lang.Integer saveArCstEntry(ArModCustomerDetails details, java.lang.String CT_NM, java.lang.String PYT_NM, java.lang.String SPL_SPPLR_CODE, java.lang.String CC_NM, java.lang.String CST_GL_COA_RCVBL_ACCNT, java.lang.String CST_GL_COA_RVNUE_ACCNT, java.lang.String CST_GL_COA_UNERND_INT_ACCNT, java.lang.String CST_GL_COA_ERND_INT_ACCNT, java.lang.String CST_GL_COA_UNERND_PNT_ACCNT, java.lang.String CST_GL_COA_ERND_PNT_ACCNT, java.lang.String BA_NM, java.lang.Integer RS_CODE, java.lang.String SLP_SLSPRSN_CODE, java.lang.String SLP_SLSPRSN_CODE2, java.lang.String LIT_NM, java.lang.String HR_DB_NM, java.lang.String HR_EMP_BIO_NMBR, java.lang.String PM_USR_EMP_NMBR, boolean isDraft, java.lang.Integer AD_BRNCH, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalNameAndAddressAlreadyExistsException, ArCCCoaGlReceivableAccountNotFoundException, ArCCCoaGlRevenueAccountNotFoundException, ArCCCoaGlUnEarnedInterestAccountNotFoundException, ArCCCoaGlEarnedInterestAccountNotFoundException, ArCCCoaGlUnEarnedPenaltyAccountNotFoundException, GlobalNoApprovalApproverFoundException, GlobalNoApprovalRequesterFoundException, ArCCCoaGlEarnedPenaltyAccountNotFoundException;

    ArModCustomerDetails getArCstByCstCode(java.lang.Integer CST_CODE, java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    boolean getArCstGlCoaRevenueAccountEnable(java.lang.Integer AD_CMPNY);

    ArModCustomerClassDetails getArCcByCcName(java.lang.String CC_NM, java.lang.Integer AD_CMPNY);

    ArModCustomerTypeDetails getArCtByCtName(java.lang.String CT_NM, java.lang.Integer AD_CMPNY);

    java.util.ArrayList getAdBrByRspnsblty(java.lang.Integer RS_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAdBrCstAll(java.lang.Integer BCST_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getAdBrResAll(int resCode, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getInvLitAll(java.lang.Integer AD_CMPNY);

}