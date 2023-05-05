package com.ejb.txnapi.ad;

import com.ejb.restfulapi.OfsApiResponse;

import jakarta.ejb.FinderException;
import jakarta.ejb.Local;

@Local
public interface AdDocumentSequenceAssignmentApiController {
    OfsApiResponse findByDcName(String documentName, Integer companyCode) throws FinderException;

    OfsApiResponse findByDcName(String documentName, String companyShortName) throws FinderException;

}