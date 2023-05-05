package com.ejb.txn.genfld;

import com.ejb.exception.gen.GenVSVNoValueSetValueFoundException;
import com.ejb.exception.global.*;
import com.util.gen.GenValueSetValueDetails;

import jakarta.ejb.Local;


@Local
public interface GenValueSetValueController {

    java.util.ArrayList getGenVsAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getGenQlfrAll(java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getGenVsvByVsName(java.lang.String VS_NM, java.lang.Integer AD_CMPNY) throws GenVSVNoValueSetValueFoundException;

    char getGenSgSegmentTypeByVsName(java.lang.String VS_NM, java.lang.Integer AD_CMPNY);

    void addGenVsvEntry(GenValueSetValueDetails details, java.lang.String VS_NM, java.lang.String QL_ACCNT_TYP, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalRecordInvalidException, GlobalSegmentValueInvalidException;

    void updateGenVsvEntry(GenValueSetValueDetails details, java.lang.String VS_NM, java.lang.String QL_ACCNT_TYP, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyExistException, GlobalRecordAlreadyAssignedException, GlobalRecordInvalidException, GlobalSegmentValueInvalidException;

    void deleteGenVsvEntry(java.lang.Integer VSV_CODE, java.lang.Integer AD_CMPNY) throws GlobalRecordAlreadyAssignedException;

}