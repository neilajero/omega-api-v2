package com.ejb.txn.ad;

import com.util.ad.AdStoredProcedureDetails;

import jakarta.ejb.Local;

@Local
public interface AdStoredProcedureSetupController {

    AdStoredProcedureDetails getAdSp(Integer companyCode);

    Integer saveAdSpEntry(AdStoredProcedureDetails details, Integer companyCode);

}