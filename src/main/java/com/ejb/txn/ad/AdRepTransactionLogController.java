package com.ejb.txn.ad;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;
import java.util.ArrayList;
import java.util.HashMap;

@Local
public interface AdRepTransactionLogController {

    ArrayList executeAdRepTransactionLog(HashMap criteria, ArrayList branchList, String orderBy, Integer companyCode) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(Integer companyCode);

}