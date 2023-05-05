package com.ejb.txn.ad;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;
import java.util.*;

@Local
public interface AdRepApprovalListController {

    ArrayList getAdApprovalDocumentAll(Integer companyCode);

    ArrayList executeAdRepApprovalList(HashMap criteria, Integer companyCode) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(Integer companyCode);

}