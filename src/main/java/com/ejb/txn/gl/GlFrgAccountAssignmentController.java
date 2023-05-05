package com.ejb.txn.gl;

import com.ejb.exception.gl.GlFrgAAAccountHighInvalidException;
import com.ejb.exception.gl.GlFrgAAAccountLowInvalidException;
import com.ejb.exception.gl.GlFrgAARowAlreadyHasCalculationException;
import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.ejb.exception.global.GlobalRecordAlreadyDeletedException;
import com.util.gl.GlFrgAccountAssignmentDetails;
import com.util.gl.GlFrgRowDetails;

import jakarta.ejb.Local;


@Local
public interface GlFrgAccountAssignmentController {

    java.util.ArrayList getGlFrgAaByRowCode(java.lang.Integer ROW_CODE, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    GlFrgRowDetails getGlFrgRowByRowCode(java.lang.Integer ROW_CODE, java.lang.Integer AD_CMPNY);

    void addGlFrgAaEntry(GlFrgAccountAssignmentDetails details, java.lang.Integer ROW_CODE, java.lang.Integer AD_CMPNY) throws GlFrgAARowAlreadyHasCalculationException, GlFrgAAAccountHighInvalidException, GlFrgAAAccountLowInvalidException;

    void updateGlFrgAaEntry(GlFrgAccountAssignmentDetails details, java.lang.Integer ROW_CODE, java.lang.Integer AD_CMPNY) throws GlFrgAAAccountHighInvalidException, GlFrgAAAccountLowInvalidException;

    void deleteGlFrgAaEntry(java.lang.Integer AA_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyDeletedException;

}