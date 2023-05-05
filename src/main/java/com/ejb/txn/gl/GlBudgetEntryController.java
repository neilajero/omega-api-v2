package com.ejb.txn.gl;

import com.ejb.exception.gl.GlBGAPasswordInvalidException;
import com.ejb.exception.global.GlobalAccountNumberInvalidException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyExistException;
import com.util.mod.gl.GlModBudgetAmountDetails;

import jakarta.ejb.Local;


@Local
public interface GlBudgetEntryController {

    java.util.ArrayList getGlBoAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGlBgtAll(java.lang.Integer AD_CMPNY);

    GlModBudgetAmountDetails getGlBgaByBgaCode(java.lang.Integer BGA_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getGlBgaTemplateByBoNameAndBgtName(java.lang.String BO_NM, java.lang.String BGT_NM, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.lang.Integer saveGlBgaEntry(GlModBudgetAmountDetails details, java.lang.String BO_NM, java.lang.String BGT_NM, java.lang.String COA_ACCNT_NMBR, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalAccountNumberInvalidException, GlBGAPasswordInvalidException;

    short getGlFcPrecisionUnit(java.lang.Integer AD_CMPNY);

}