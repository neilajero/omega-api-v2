package com.ejb.txn.gl;

import com.ejb.exception.global.GlobalNoRecordFoundException;

import jakarta.ejb.Local;


@Local
public interface GlFindSegmentController {

    java.util.ArrayList getGlValueSetValueByCriteria(java.util.HashMap criteria, java.util.ArrayList vsvDescList, short selectedCoaSegmentNumber, java.util.ArrayList adBrnchList, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    java.util.ArrayList getGenVsAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList getGenVsvAllByVsNameAndVsvDesc(java.lang.String VS_NM, java.lang.String VSV_DESC, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

}