package com.ejb.txn.ad;

import com.util.ad.AdApprovalDetails;

import jakarta.ejb.Local;

@Local
public interface AdApprovalSetupController {

    AdApprovalDetails getAdApr(Integer companyCode);

    Integer saveAdAprEntry(AdApprovalDetails details, Integer companyCode);

}