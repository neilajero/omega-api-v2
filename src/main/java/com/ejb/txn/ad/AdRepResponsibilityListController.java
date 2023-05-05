package com.ejb.txn.ad;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;
import java.util.*;

@Local
public interface AdRepResponsibilityListController {

    ArrayList executeAdRepResponsibilityList(HashMap criteria, ArrayList branchList, String orderBy, boolean showFormFunction, Integer companyCode) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(Integer companyCode);

    ArrayList getAdBrResAll(Integer responsibilityCode, Integer companyCode) throws GlobalNoRecordFoundException;
}