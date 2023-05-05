package com.ejb.txn.genfld;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;


@Local
public interface GenRepValueSetValueListController {

    java.util.ArrayList getGenVsAll(java.lang.Integer AD_CMPNY);

    java.util.ArrayList executeGenRepValueSetValueList(java.util.HashMap criteria, java.lang.String ORDER_BY, java.lang.Integer AD_CMPNY) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(java.lang.Integer AD_CMPNY);

}