package com.ejb.txn.ad;

import com.ejb.exception.global.GlobalNoRecordFoundException;
import com.util.ad.AdApprovalDocumentDetails;

import jakarta.ejb.Local;
import java.util.ArrayList;

@Local
public interface AdApprovalDocumentController {
    ArrayList getAdAdcAll(Integer companyCode) throws GlobalNoRecordFoundException;

    void updateAdAdcEntry(AdApprovalDocumentDetails details, Integer companyCode);
}