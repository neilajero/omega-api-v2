package com.ejb.txn.ad;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdCompanyDetails;

import jakarta.ejb.Local;
import java.util.*;

@Local
public interface AdRepUserListController {

    ArrayList executeAdRepUserList(HashMap criteria, String orderBy, Integer companyCode) throws GlobalNoRecordFoundException;

    AdCompanyDetails getAdCompany(Integer companyCode);

}